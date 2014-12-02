# Refactoring Dojo

The purpose of this project is to train your **refactoring skills** in Scala.

You will find an implemented coding kata (e.g. Conway's game of life) together with acceptance-tests.

Your task is to refactor the implementation and produce a much nicer code-base. The acceptance-tests are there to guide you and let you know if you broke anything.

# Included Katas

## Game of Life

So far, the only kata implemented is "Coway's game of life". 

It simulates a two-dimensional world / board, where a position can be occupied by a cell. A cell can be dead or alive (others prefer to think in terms of a position being empty or not).

### Rules

1. Any live cell with less than two live neighbours dies, as if caused by under-population.
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overcrowding.
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

# Idiomatic and clean (Scala) code

In order to help you, here is a partial list of what you can expect to find, and how to makt it better...

* Prefer constants to variables (`val` vs. `var`)
* Prefer immutable data structures
* Prefer domain objects to generic data structures (e.g. Map)
* Prefer immutable objects
* Don't misuse exceptions for flow-control
* Avoid type-casting
* Prefer for/foreach/map/etc. over while-loops
* Strive for good intention-revealing names
* Avoid "magic numbers"
* Prefer Option over null
* Split logic into smaller pieces
* Strive for same levels of abstraction
* Strive for cohesion
* Remove dead code-branches
* Some comments are a code smell

# Disclaimer

The code in this project was made horrendous on purpose. It does not represent the normal coding style of the author(s).