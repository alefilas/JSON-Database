JSON Database
============
It's a client-server application for data storage. It stores data and receives requests in JSON format.
The request to the server is a JSON view:
````
{"type":"set", "key":"1", "value":"hello"}
````
Client starts with 4 arguments:
* -in path to file with query
* -type type of query(set, get or delete)
* -key  
* -value (only if type is set)