# Updating javadoc on public repo

	cd MobileBuy
	./gradlew javadoc
	git checkout gh-pages
	git add buy/build/docs/javadoc/*
	git commit -m 'update javadoc'
	git push