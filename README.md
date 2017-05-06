# scoreboard

## Usage

If you haven't created the docker image already, run the following
command. You will only need to do this once.

```BASH
docker build -t scoreboard .
```

Place your CSV and VIDEO file in the `resources/in/` directory and
then run program:

```BASH
docker run --rm -it \
    -v $(pwd)/resources/out:/usr/src/scoreboard/resources/out \
    -v $(pwd)/resources/in:/usr/src/scoreboard/resources/in \
    scoreboard
```
