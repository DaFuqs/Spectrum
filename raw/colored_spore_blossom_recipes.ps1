$c = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$c | Foreach-Object {
New-Item -Name "$_`_glowblock.json" -ItemType File -Value @"
{
  "type": "spectrum_pedestal",
  "time": 200,
  "tier": "advanced",
  "magenta": 0,
  "yellow": 0,
  "cyan": 0,
  "black": 0,
  "white": 0,
  "experience": 1.0,
  "pattern": [
    "PPP",
    "PQP",
    "PPP"
  ],
  "key": {
  	"P": {
      "item": "spectrum:$_`_pigment"
    },
	"Q": {
      "item": "spectrum:quitoxic_reeds"
    }
  },
  "result" : {
    "item": "spectrum:$_`_glowblock",
    "count": 4
  },
  "required_advancements": ["spectrum:hidden/collect_pigment/collect_$_`_pigment"]
}
"@

}