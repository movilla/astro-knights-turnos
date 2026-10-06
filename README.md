# Astro Knights – Turnos

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

## Compilar
Abre la carpeta en Android Studio (JDK 17), deja que sincronice Gradle y pulsa Run, o:
`./gradlew assembleDebug` → app/build/outputs/apk/debug/app-debug.apk

## Primera y última versión :]
https://github.com/movilla/astro-knights-turnos/actions/runs/37436567181/artifacts/11398937464
