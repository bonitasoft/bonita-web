launch less in dev mode

run
'''
mvn jetty:run -Pcss
'''

## Compile the less without maven

### Requirement

- nodejs + npm
- less `npm i -g lessc`
- autoprefixer `npm i -g autoprefixer` (not required)

```sh
# Without autoprefixer (bad)
$ lessc skin/bootstrap/skin.less > test.css

# It's better with autoprefixer
$ lessc skin/bootstrap/skin.less | autoprefixer > test.css
```
