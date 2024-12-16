ARCHIVE = archive.tar.gz
INVALID_ARCHIVE = invalid.tar.gz
EMPTY_ARCHIVE = empty.tar.gz

SHOWCASE_ARCHIVE_1 = showcase1.tar.gz
SHOWCASE_ARCHIVE_2 = showcase2.tar.gz

.PHONY: archive
archive:
	tar czf ${ARCHIVE} -C sample/data .

.PHONY: invalid-archive
invalid-archive:
	tar czf ${INVALID_ARCHIVE} -C sample/invalid .

.PHONY: empty-archive
empty-archive:
	touch ${EMPTY_ARCHIVE}

.PHONY: showcase-archives
showcase-archives:
	tar czf ${SHOWCASE_ARCHIVE_1} -C sample/showcase1 .
	tar czf ${SHOWCASE_ARCHIVE_2} -C sample/showcase2 .
