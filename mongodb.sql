//start mongodb
mongo -u admin -p admin123 --authenticationDatabase admin

//show databases
show dbs

//show collections
show collections

//select database
use dbname

//entry of data
db.collecctionName.insertOne({data})
db.collecctionName.insert({data})

//find data of a collection
db.collectionName.find()
db.collectionName.find(filter,option)

//returns the first one, which matches with criteria
db.collectionName.findOne()
db.collectionName.findOne(filter,option)

//delete data
db.collectionName.remove({})
db.collectionName.remove({data})

//drop table
db.collectionName.drop()


