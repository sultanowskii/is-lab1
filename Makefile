ARCHIVE = archive.tar.gz
INVALID_ARCHIVE = invalid.tar.gz
EMPTY_ARCHIVE = empty.tar.gz

.PHONY: archive
archive:
	tar czf ${ARCHIVE} sample/data/*.yaml sample/data/*.yml

.PHONY: invalid-archive
invalid-archive:
	tar czf ${INVALID_ARCHIVE} sample/invalid/*.yaml

.PHONY: empty-archive
empty-archive:
	touch ${EMPTY_ARCHIVE}
