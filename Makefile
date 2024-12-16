ARCHIVE = archive.tar.gz
INVALID_ARCHIVE = invalid.tar.gz
EMPTY_ARCHIVE = empty.tar.gz

.PHONY: archive
archive:
	tar czf ${ARCHIVE} -C sample/data .

.PHONY: invalid-archive
invalid-archive:
	tar czf ${INVALID_ARCHIVE} -C sample/invalid .

.PHONY: empty-archive
empty-archive:
	touch ${EMPTY_ARCHIVE}
