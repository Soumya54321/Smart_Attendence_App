const express=require('express');
const app=express()
const bcrypt=require('bcrypt')
const bodyParser=require('body-parser');
const cors=require('cors');
const mongo = require('mongodb').MongoClient;

const PORT=3000;
//const api=require('./routes/api');
//app.use('/api',api);

app.set('view-engine','ejs')
app.use(express.urlencoded({extended:false}))

app.use(bodyParser.json());

//support parsing of application
app.use(bodyParser.urlencoded({ extended: true }));

//connect with mongodb and create database
mongo.connect('mongodb://localhost:27017/institute',function(err,client){
    if(err ) throw err;
    console.log('Connected to mongodb');

    var db = client.db('institute');
    teachers=db.collection('teachers');
    students=db.collection('students');

    app.get('/',(req,res)=>{
        //res.render('app.ejs',{name: "Soumya"})
    })
    
    app.get('/login',(req,res)=>{
        res.render('login.ejs')
    })
    
    app.get('/register',(req,res)=>{
        res.render('register.ejs')
    })

    app.post('/register',(req,res)=>{
        let userData=req.body;
        teachers.find({email:userData.email}).toArray(function(err,response){
            if(!response[0]){
                teachers.insert(userData,function(){
                    data={success:1};
                    res.status(200).send(data);
                    console.log(userData)
                });
            }else{
                data={success:0};
                res.status(200).send(data);
            }
        });
    });

    app.post('/login',(req,res)=>{
        let userData=req.body;

        if(userData.email!=""){
            teachers.find({email:userData.email}).toArray(function(err,response){
                if(!response[0]){
                    var data={success:0};
                    res.status(200).send(data);
                }else{
                    var data=response[0]
                    res.status(200).send(data);
                    console.log(data)
                }
            });
        }
    })
})

app.listen(PORT,function(req,res){
    console.log('Server is running on port: '+PORT);
});
