@Grapes([
    @Grab(group='org.apache.commons', module='commons-lang3', version='3.13.0')
])

import org.apache.commons.lang3.*

File safeCanonicalFile(File f) {
    try {
        return f.getCanonicalFile()
    } catch (IOException ignore) {
        // skip canonicalization then, it may not exist yet
        return f
    }
}

File getGroovyRoot() {
    String root = System.getProperty('groovy.root')
    def groovyRoot
    if (root == null) {
        groovyRoot = new File(System.getProperty('user.home'), '.groovy')
    } else {
        groovyRoot = new File(root)
    }
    safeCanonicalFile(groovyRoot)
}

File getGrapeDir() {
    String root = System.getProperty('grape.root')
    if (root == null) {
        return getGroovyRoot()
    }
    safeCanonicalFile(new File(root))
}

File getGrapeCacheDir() {
    File cache = new File(getGrapeDir(), 'grapes')
    if (!cache.exists()) {
        cache.mkdirs()
    } else if (!cache.isDirectory()) {
        throw new RuntimeException("The grape cache dir $cache is not a directory")
    }
    cache
}

if (getGrapeCacheDir().listFiles().size() == 0) {
    throw new RuntimeException('No Grapes files')
}
