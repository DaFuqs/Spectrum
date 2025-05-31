package de.dafuqs.spectrum.api.energy.extra;

import de.dafuqs.spectrum.api.energy.color.InkColor;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.Iterators;

import java.util.Iterator;

@SuppressWarnings("UnstableApiUsage")
public abstract class BindingInkStorage implements Storage<InkColor>, TransactionContext.OuterCloseCallback {
    protected final Storage<InkColor> wrapped;
    // Since we don't need to care about storing snapshots, we can do this instead,
    // assuming the user doesn't do unorthodox transaction tricks which would break most storage impls anyway.
    // The kind that would break default Fabric storage APIs, that is, which nobody in their sane mind would do.
    protected boolean addOuterClose = true;

    public BindingInkStorage(Storage<InkColor> wrapped) {
        this.wrapped = wrapped;
    }

    public abstract void markDirty();

    @Override
    public void afterOuterClose(TransactionContext.Result result) {
        // only mark dirty if the full transaction actually went through
        if (result.wasCommitted()) markDirty();
        this.addOuterClose = true;
    }

    public void bookkeeping(TransactionContext transaction) {
        if (addOuterClose) {
            transaction.addOuterCloseCallback(this);
            addOuterClose = false;
        }
    }

    public Storage<InkColor> unwrap() {
        return wrapped;
    }

    @Override
    public long insert(InkColor resource, long maxAmount, TransactionContext transaction) {
        bookkeeping(transaction);
        return wrapped.insert(resource, maxAmount, transaction);
    }

    @Override
    public long extract(InkColor resource, long maxAmount, TransactionContext transaction) {
        bookkeeping(transaction);
        return wrapped.extract(resource, maxAmount, transaction);
    }

    @Override
    public long getVersion() {
        return wrapped.getVersion();
    }

    public class ViewWrapper implements StorageView<InkColor> {

        protected final StorageView<InkColor> wrappedView;

        public ViewWrapper(StorageView<InkColor> toWrap) {
            this.wrappedView = toWrap;
        }

        @Override
        public long extract(InkColor resource, long maxAmount, TransactionContext transaction) {
            bookkeeping(transaction);
            return wrappedView.extract(resource, maxAmount, transaction);
        }

        @Override
        public boolean isResourceBlank() {
            return wrappedView.isResourceBlank();
        }

        @Override
        public InkColor getResource() {
            return wrappedView.getResource();
        }

        @Override
        public long getAmount() {
            return wrappedView.getAmount();
        }

        @Override
        public long getCapacity() {
            return wrappedView.getCapacity();
        }

        @Override
        public StorageView<InkColor> getUnderlyingView() {
            return wrappedView;
        }
    }

    @Override
    public @NotNull Iterator<StorageView<InkColor>> iterator() {
        return Iterators.transform(wrapped.iterator(), ViewWrapper::new);
    }
}
