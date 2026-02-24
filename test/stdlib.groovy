import groovy.json.JsonSlurper
import groovy.xml.XmlSlurper

// JSON Verification
def jsonInput = '{"project": "groovy", "modules": ["json", "xml"]}'
def json = new JsonSlurper().parseText(jsonInput)
assert json.project == 'groovy'
assert json.modules.size() == 2
assert json.modules.contains('json')
assert json.modules.contains('xml')

// XML Verification
def xmlInput = '<project name="groovy"><modules><module>json</module><module>xml</module></modules></project>'
def xml = new XmlSlurper().parseText(xmlInput)
assert xml.@name == 'groovy'
assert xml.modules.module.size() == 2
assert xml.modules.module.find { it.text() == 'json' } != null
assert xml.modules.module.find { it.text() == 'xml' } != null

println "Groovy Standard Library (JSON/XML) verified"
