$c = @("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow")

$c | Foreach-Object {
New-Item -Name "$_`_lamp.json" -ItemType File -Value @"
{
  "type": "pigment_altar",
  "time": 40,
  "tier": 1,
  "magenta": 0,
  "yellow": 0,
  "cyan": 0,
  "black": 0,
  "white": 0,
  "experience": 0.25,
  "pattern": [
    "SPS",
    "PGP",
    "SPS"
  ],
  "key": {
  	"P": {
      "item": "pigment:$_`_pigment"
    },
	"G": {
      "item": "minecraft:glowstone"
    },
	"S": {
      "item": "pigment:sparklestone_gem"
    }
  },
  "result" : {
    "item": "pigment:$_`_lamp",
    "count": 1
  }
}
"@

}