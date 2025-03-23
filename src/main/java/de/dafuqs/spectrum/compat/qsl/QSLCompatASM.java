package de.dafuqs.spectrum.compat.qsl;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.util.Bytecode;

import java.lang.invoke.*;

public class QSLCompatASM {
	/* FAPI-specific class name constants */
	private static final String TOREMAP_TRISTATE_CLASS_NAME_INTERNAL = "net/fabricmc/fabric/api/util/TriState";
	private static final String TOREMAP_TRISTATE_CLASS_NAME          = "L" + TOREMAP_TRISTATE_CLASS_NAME_INTERNAL + ";";

	/* QSL-specific class name constants */
	private static final String REMAPPED_TRISTATE_CLASS_NAME_INTERNAL = "org/quiltmc/qsl/base/api/util/TriState";
	private static final String REMAPPED_TRISTATE_CLASS_NAME          = "L" + REMAPPED_TRISTATE_CLASS_NAME_INTERNAL + ";";

	private static final String STATUS_EFFECT_EVENTS_NAME_INTERNAL    = "org/quiltmc/qsl/entity/effect/api/StatusEffectEvents";

	private static final String REMOVAL_REASON_CLASS_NAME             = "Lorg/quiltmc/qsl/entity/effect/api/StatusEffectRemovalReason;";
	private static final String QSL_EVENT_CLASS_NAME_INTERNAL         = "org/quiltmc/qsl/base/api/event/Event";
	private static final String QSL_EVENT_CLASS_NAME                  = "L" + QSL_EVENT_CLASS_NAME_INTERNAL + ";";
	private static final String QSL_EVENT_REGISTER_METHOD_NAME        = "register";
	private static final String QSL_EVENT_REGISTER_METHOD_DESC        = Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(Object.class));

	private static final String TARGET_IFACE_NAME            = "ShouldRemove";
	private static final String TARGET_IFACE_CLASS_NAME_FULL = STATUS_EFFECT_EVENTS_NAME_INTERNAL + "$" + TARGET_IFACE_NAME;
	private static final String TARGET_EVENT_FIELD_NAME      = "SHOULD_REMOVE";
	private static final String TARGET_IFACE_METHOD_NAME     = "shouldRemove";
	private static       String TARGET_IFACE_METHOD_DESC     = ""; // non-final; populated on transform due to mappings

	/* mixin-specific constants */
	private static final String TARGET_CLASS_NAME       = "de/dafuqs/spectrum/registries/SpectrumEventListeners";
	private static final String TARGET_METHOD_NAME      = "register";
	private static final String MIXIN_CLASS_NAME_DOTS   = "de.dafuqs.spectrum.mixin.compat.quilt_status_effect.present.SpectrumEventListenersMixin";
	private static final String TARGET_ASM_METHOD_NAME  = "_shouldRemove";
	private static final String TRANSFORMED_METHOD_NAME = "QSL_shouldRemove";

	/* generic constants */
	private static final Handle LMF_HANDLE = new Handle(Opcodes.H_INVOKESTATIC, Type.getInternalName(LambdaMetafactory.class),
			"metafactory", Bytecode.generateDescriptor(CallSite.class, MethodHandles.Lookup.class, String.class,
			MethodType.class, MethodType.class, MethodHandle.class, MethodType.class), false);

	public static String transformMethodDesc(String originalDescriptor) {
		var methodArgs = Type.getArgumentTypes(originalDescriptor);
		// check if method args are correct just in case
		assert methodArgs.length == 3; assert methodArgs[2].equals(Type.getType(Object.class));
		methodArgs[2]  = Type.getType(REMOVAL_REASON_CLASS_NAME); // replace 3rd argument with removal reason type
		var newDescriptor = Type.getMethodDescriptor(Type.getType(REMAPPED_TRISTATE_CLASS_NAME), methodArgs);
		TARGET_IFACE_METHOD_DESC = newDescriptor; // set the global to the freshly-baked descriptor for later use
		return newDescriptor;
	}

	public static MethodVisitor transformerVisitor(MethodVisitor orig) {
		return new MethodVisitor(Opcodes.ASM9, orig) {
			@Override
			public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
				// in this case the owner is TOREMAP_TRISTATE_CLASS_NAME_INTERNAL and the (dubiously implied) descriptor is TOREMAP_TRISTATE_CLASS_NAME, though check the descriptor just in case
				// replace with:    owner == REMAPPED_TRISTATE_CLASS_NAME_INTERNAL                            descriptor == REMAPPED_TRISTATE_CLASS_NAME
				if (opcode == Opcodes.GETSTATIC && owner.equals(TOREMAP_TRISTATE_CLASS_NAME_INTERNAL) && descriptor.equals(TOREMAP_TRISTATE_CLASS_NAME)) {
					owner      = REMAPPED_TRISTATE_CLASS_NAME_INTERNAL;
					descriptor = REMAPPED_TRISTATE_CLASS_NAME;
				}
				super.visitFieldInsn(opcode, owner, name, descriptor);
			}
		};
	}

	public static void applyOnRemovedMethodTransform(ClassNode targetClass, MethodNode targetMethod) {
		/* _shouldRemove: transform TriState type used within into quilt equivalent and turn 3rd argument [reason] into appropriate quilt type */
		/* write remapped version into QSL_shouldRemove */
		targetMethod.accept(new ClassVisitor(Opcodes.ASM9, targetClass) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
				return transformerVisitor(super.visitMethod(access, TRANSFORMED_METHOD_NAME, transformMethodDesc(descriptor), signature, exceptions));
			}
		});
		// remove original method as it is unused
		targetClass.methods.remove(targetMethod);
	}

	public static void applyRegisterTransformations(MethodNode registerMethod) {
		// graciously stolen from BeforeFinalReturn.find
		AbstractInsnNode returnOpcode = null;
		int op = Type.getReturnType(registerMethod.desc).getOpcode(Opcodes.IRETURN);
		for (AbstractInsnNode insn : registerMethod.instructions)
			if (insn instanceof InsnNode && insn.getOpcode() == op) returnOpcode = insn;
		assert returnOpcode != null;

		// create separate insn batch
		var addedInsns = new InsnList();
		/* register: get StatusEffectEvents.SHOULD_REMOVE */
		var eventField = new FieldInsnNode(Opcodes.GETSTATIC, STATUS_EFFECT_EVENTS_NAME_INTERNAL, TARGET_EVENT_FIELD_NAME, QSL_EVENT_CLASS_NAME);
		/* register: emit INDY to create instance of org.quiltmc.qsl.entity.effect.api.StatusEffectEvents$ShouldRemove via QSL_shouldRemove */
		var targetIfaceDesc = Type.getMethodType(TARGET_IFACE_METHOD_DESC);
		var targetImplDesc = new Handle(Opcodes.H_INVOKESTATIC, TARGET_CLASS_NAME, TRANSFORMED_METHOD_NAME, TARGET_IFACE_METHOD_DESC, false);
		var indy = new InvokeDynamicInsnNode(TARGET_IFACE_METHOD_NAME,                      // `interfaceMethodName`
				Type.getMethodDescriptor(Type.getObjectType(TARGET_IFACE_CLASS_NAME_FULL)), // `factoryType`
				LMF_HANDLE,                                                                 // `metafactory` handle
				targetIfaceDesc,                                                            // `interfaceMethodType`
				targetImplDesc,                                                             // `implementation`
				targetIfaceDesc                                                             // `dynamicMethodType`
		);
		/* register: invoke [INVOKEVIRTUAL] org.quiltmc.qsl.entity.effect.api.StatusEffectEvents.SHOULD_REMOVE.register(<LAMBDA>) */
		var invokeRegister = new MethodInsnNode(Opcodes.INVOKEVIRTUAL, QSL_EVENT_CLASS_NAME_INTERNAL, QSL_EVENT_REGISTER_METHOD_NAME, QSL_EVENT_REGISTER_METHOD_DESC);
		// add instructions in batch
		addedInsns.add(eventField);
		addedInsns.add(indy);
		addedInsns.add(invokeRegister);
		// add ASM'd instructions into method body and check
		registerMethod.instructions.insertBefore(returnOpcode, addedInsns);
		registerMethod.check(Opcodes.ASM9);
	}

	public static void transformSpectrumEventListeners(ClassNode targetClass) {
		MethodNode targetMethod = null;
		MethodNode registerMethod = null;
		for (var method : targetClass.methods) {
			if (method.name.equals(TARGET_ASM_METHOD_NAME))  targetMethod = method;
			else if (method.name.equals(TARGET_METHOD_NAME)) registerMethod = method;
		}
		assert targetMethod   != null; applyOnRemovedMethodTransform(targetClass, targetMethod);
		assert registerMethod != null; applyRegisterTransformations(registerMethod);
	}

	public static void postApply(ClassNode targetClass, String mixinClassName) {
		if (mixinClassName.equals(MIXIN_CLASS_NAME_DOTS)) transformSpectrumEventListeners(targetClass);
	}
}
