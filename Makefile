ARCHIVE = archive.tar.gz

.PHONY: archive
archive:
	tar czf ${ARCHIVE} example/*.yaml
