# Astro Knights – Turn Order

🇬🇧 English · [🇪🇸 Castellano](#astro-knights--turnos)

Android app that simulates the Astro Knights turn order deck (based on the Aeon's End turn order deck app).

## Usage
- Tap anywhere on the screen to draw the next card.
- Top left: undo the last draw (within the same pass through the deck).
- Top right: settings (number of players, names, reset deck).
- When the deck runs out and another card must be drawn, it is reshuffled (as the rules state).

## Deck composition (always 2 Boss cards)
- 1 player: 3 player cards.
- 2 players: 2 cards per player.
- 3 players: 1 card per player + a wild card (the wild token passes to the player on the left each time it comes up).
- 4 players: 2 "1/2" cards + 2 "3/4" cards (the first time, the pair chooses who goes; the second time, the other one goes).

## Languages
The app follows the language of your device: Spanish if your phone is set to Spanish, English in every other case. Strings live in `app/src/main/res/values/` (English, default) and `app/src/main/res/values-es/` (Spanish). To add another language, copy `values/strings.xml` into a new `values-xx/` folder (e.g. `values-fr/`) and translate it.

## Build
Open the folder in Android Studio (JDK 17), let Gradle sync and press Run, or:
`./gradlew assembleDebug` → app/build/outputs/apk/debug/app-debug.apk

## First and latest version :]
https://github.com/movilla/astro-knights-turnos/actions/runs/37647792719/artifacts/11494214972

## License
GNU AGPL-3.0, see [LICENSE](LICENSE).

---

# Astro Knights – Turnos

[🇬🇧 English](#astro-knights--turn-order) · 🇪🇸 Castellano

App Android que simula el mazo de orden de turnos de Astro Knights (basada en la app de mazo de turnos de Aeon's End).

## Uso
- Toca en cualquier parte de la pantalla para robar la siguiente carta.
- Arriba a la izquierda: deshacer el último robo (dentro de la misma pasada del mazo).
- Arriba a la derecha: ajustes (número de jugadores, nombres, reiniciar mazo).
- Cuando el mazo se agota y hay que robar otra carta, se baraja de nuevo (como indica el reglamento).

## Composición del mazo (siempre 2 cartas de Jefe)
- 1 jugador: 3 cartas de jugador.
- 2 jugadores: 2 cartas por jugador.
- 3 jugadores: 1 carta por jugador + carta comodín (la ficha comodín pasa al jugador de la izquierda cada vez que sale).
- 4 jugadores: 2 cartas "1/2" + 2 cartas "3/4" (la primera vez eligen quién juega; la segunda juega el otro).

## Idiomas
La app sigue el idioma del dispositivo: castellano si el móvil está en castellano, e inglés en cualquier otro caso. Los textos están en `app/src/main/res/values/` (inglés, por defecto) y `app/src/main/res/values-es/` (castellano). Para añadir otro idioma, copia `values/strings.xml` en una carpeta nueva `values-xx/` (por ejemplo `values-fr/`) y tradúcelo.

## Compilar
Abre la carpeta en Android Studio (JDK 17), deja que sincronice Gradle y pulsa Run, o:
`./gradlew assembleDebug` → app/build/outputs/apk/debug/app-debug.apk

## Primera y última versión :]
https://github.com/movilla/astro-knights-turnos/actions/runs/37647792719/artifacts/11494214972

## Licencia
GNU AGPL-3.0, véase [LICENSE](LICENSE).
