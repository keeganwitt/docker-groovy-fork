@Grapes([
    @Grab(group='org.apache.commons', module='commons-lang3', version='3.13.0')
])

import org.apache.commons.lang3.*

File getGroovyRoot() {
    String root = System.getProperty('groovy.root')
    def groovyRoot
    if (root == null) {
        groovyRoot = new File(System.getProperty('user.home'), '.groovy')
    } else {
        groovyRoot = new File(root)
    }
    try {
        groovyRoot = groovyRoot.getCanonicalFile()
    } catch (IOException ignore) {
        // skip canonicalization then, it may not exist yet
    }
    groovyRoot
}

File getGrapeDir() {
    String root = System.getProperty('grape.root')
    if (root == null) {
        return getGroovyRoot()
    }
    File grapeRoot = new File(root)
    try {
        grapeRoot = grapeRoot.getCanonicalFile()
    } catch (IOException ignore) {
        // skip canonicalization then, it may not exist yet
    }
    grapeRoot
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

void withSystemProperties(Map props, Closure cls) {
    Map oldProps = [:]
    props.each { k, v ->
        oldProps[k] = System.getProperty(k)
        if (v == null) {
            System.clearProperty(k)
        } else {
            System.setProperty(k, v)
        }
    }
    try {
        cls()
    } finally {
        oldProps.each { k, v ->
            if (v == null) {
                System.clearProperty(k)
            } else {
                System.setProperty(k, v)
            }
        }
    }
}

// Test getGrapeDir() scenarios
String tempDir = System.getProperty('java.io.tmpdir')
withSystemProperties(['grape.root': tempDir + '/grape']) {
    File grapeDir = getGrapeDir()
    assert grapeDir.absolutePath == new File(tempDir, 'grape').canonicalFile.absolutePath
}

withSystemProperties(['grape.root': null, 'groovy.root': tempDir + '/groovy']) {
    File grapeDir = getGrapeDir()
    assert grapeDir.absolutePath == new File(tempDir, 'groovy').canonicalFile.absolutePath
}

withSystemProperties(['grape.root': null, 'groovy.root': null]) {
    File grapeDir = getGrapeDir()
    File expected = new File(System.getProperty('user.home'), '.groovy').canonicalFile
    assert grapeDir.absolutePath == expected.absolutePath
}

if (getGrapeCacheDir().listFiles().size() == 0) {
    throw new RuntimeException('No Grapes files')
}
