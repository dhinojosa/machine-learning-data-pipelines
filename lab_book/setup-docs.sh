docker run --rm -v "$(pwd)":/documents/ -v "$(pwd)"/images:/documents/images \
asciidoctor/docker-asciidoctor asciidoctor -b html index.adoc
