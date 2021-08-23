$c = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$c | Foreach-Object {
New-Item -Name "$_`_spore_blossom.json" -ItemType File -Value @"
{
  "type": "spectrum_pedestal",
  "time": 80,
  "tier": "simple",
  "magenta": 0,
  "yellow": 0,
  "cyan": 0,
  "black": 0,
  "white": 0,
  "experience": 0.5,
  "pattern": [
    "PPP",
    "PSP",
    "PPP"
  ],
  "key": {
  	"P": {
      "item": "spectrum:$_`_pigment"
    },
	"S": {
      "item": "minecraft:spore_blossom"
    }
  },
  "result" : {
    "item": "spectrum:$_`_spore_blossom",
    "count": 1
  }
}
"@

}