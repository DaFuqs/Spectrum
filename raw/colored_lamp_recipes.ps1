$c = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$c | Foreach-Object {
New-Item -Path "I:\FabricModding\Spectrum\src\main\resources\assets\spectrum\models\block" -Name "redstone_transceiver_channel_$_`.json" -ItemType File -Value @"
{
	"credit": "Made with Blockbench",
	"ambientocclusion": false,
	"textures": {
		"3": "spectrum:block/$_`_block"
	},
	"elements": [
		{
			"from": [4, 2, 4],
			"to": [12, 3, 12],
			"faces": {
				"north": {"uv": [0, 0, 8, 1], "texture": "#3"},
				"east": {"uv": [0, 0, 8, 1], "texture": "#3"},
				"south": {"uv": [0, 0, 8, 1], "texture": "#3"},
				"west": {"uv": [0, 0, 8, 1], "texture": "#3"},
				"up": {"uv": [0, 0, 8, 8], "texture": "#3"},
				"down": {"uv": [0, 0, 6, 6], "texture": "#3"}
			}
		}
	]
}
"@

}