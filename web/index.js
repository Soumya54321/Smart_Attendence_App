const express=require('express');
const app=express()
const bcrypt=require('bcrypt')
const bodyParser=require('body-parser');
const cors=require('cors');
const mongo = require('mongodb').MongoClient;
var nodemailer = require('nodemailer');
const Verifier = require("email-verifier");
var http = require("http");
var multer  =   require('multer');
var fs = require("fs");

const PORT=3000;
//const api=require('./routes/api');
//app.use('/api',api);

var storage =   multer.diskStorage({
    destination:'./allImages/',
    filename: function (req, file, callback) {
        callback(null, file.originalname);
    }
});

var upload = multer({ storage : storage});

app.set('view-engine','ejs')
app.use(express.urlencoded({extended:false}))

app.use(bodyParser.json());

//support parsing of application
app.use(bodyParser.urlencoded({ extended: true }));

//connect with mongodb and create database
mongo.connect('mongodb+srv://user:1234@cluster0-b4ebw.mongodb.net/institute?retryWrites=true&w=majority',function(err,client){
    if(err ) throw err;
    console.log('Connected to mongodb');

    //Create collections
    var db = client.db('institute');
    teachers=db.collection('teachers');
    students=db.collection('students');
    attendance=db.collection('year_2020');
    admin=db.collection('admin');

    app.use(express.static(__dirname+'/public'))

    app.get('/',(req,res)=>{
        res.render('home.ejs')
    })

    app.get('/home',(req,res)=>{
        res.render('home.ejs')
    })

    //Admin part
    app.get('/admin/login',(req,res)=>{
        res.render('login_admin.ejs')
    })
    app.get('/admin/teacher/register',(req,res)=>{
        res.render('register_admin_teacher.ejs')
    })
    app.get('/admin/student/register',(req,res)=>{
        res.render('register_admin_student.ejs')
    })
    app.get('/admin/dashboard',(req,res)=>{
        res.render('dashboard_admin.ejs')
    })

    
    //Teacher part
    app.get('/teacher/register',(req,res)=>{
        res.render('register_teacher.ejs')
    })

    app.get('/teacher/login',(req,res)=>{
        res.render('login_teacher.ejs')
    })

    app.get('/teacher/dashboard',(req,res)=>{
        res.render('dashboard_teacher.ejs')
    })

    
    //Student part
    app.get('/student/login',(req,res)=>{
        res.render('login_student.ejs')
    })

    app.get('/student/register',(req,res)=>{
        res.render('register_student.ejs')
    })

    app.get('/student/dashboard',(req,res)=>{
        res.render('dashboard_student.ejs')
    })


    //Teacher registration
    app.post('/teacher_register',(req,res)=>{
        
        let userData=req.body;
       
        teachers.find({email:userData.email}).toArray(function(err,response){
           if(!response[0]){
                //insert into db
                teachers.insert(userData,function(){
                    data={success:1};
                    res.status(200).send(data)
                    console.log(userData)

                    //Checking validity of teacher's mail id 
                    let verifier = new Verifier('at_MvCy1d8k9nBYSTQec717nWmVOZhfu')
                    verifier.verify(userData.email, (err, data) => {
                        if (err) throw err;
                        console.log(data);
                    });

                    var transporter = nodemailer.createTransport({
                        service: 'gmail',
                        host: 'smtp.googlemail.com', // Gmail Host
                        port: 465, // Port
                        secure: true, // this is true as port is 465
                        auth: {
                          user: 'yourmain@gmail.com',
                          pass: 'password'
                        }
                    });
    
                    //sending varification code(OTP) to teacher
                    var mailOptions = {
                        from: 'yourmail@gmail.com',
                        to: userData.email,
                        subject: 'Sending Email using Node.js',
                        text: 'That was easy!'
                    };
    
                    transporter.sendMail(mailOptions, function(error, info){
                        if (error) {
                            console.log(error);
                        } else {
                            console.log('Email sent: ' + info.response);
                        }
                    });   
                });
            }else{
                data={success:0};
                res.status(200).send(data);
            }
        });
    });

    //Student registration
    app.post('/student_register',upload.single('image'),(req,res)=>{
        let userData=req.body;
        students.find({contact:userData.contact,roll:userData.roll}).toArray(function(err,response){
            if(!response[0]){

                //insert into db
                students.insert(userData,function(){
                    data={success:1};
                    res.status(200).send(data)
                    console.log(userData)

                });
            }else{
                data={success:0};
                res.status(200).send(data);
            }
        });
    });

    //Admin login
    app.post('/admin_login',(req,res)=>{
        let userData=req.body;

        admin.find(userData).toArray(function(err,response){
            
            if(!response[0]){
                var data={success:false};
                res.status(200).send(data);
            }else{
                console.log(response[0])
                var data={
                    success:true,
                    key:response[0].key
                }
                console.log(data)
                res.status(202).sned(data);
            }
        })
    })

    //Teacher login
    app.post('/teacher_login',(req,res)=>{
        let userData=req.body;

        if(userData.email!=""){
            teachers.find({email:userData.email,
                           password: userData.password}).toArray(function(err,response){
                if(!response[0]){
                    var data={success:false};
                    res.status(200).send(data);
                }else{
                    var data={success: true,
                        email: response[0].email,
                        name: response[0].name,
                        dept: response[0].department}
                    res.status(200).send(data);
                    console.log(data)
                }
            });
        }
    })

    //Student login 
    app.post('/student_login',(req,res)=>{
        let userData=req.body;

        if(userData.roll!=""){
            students.find(userData).toArray(function(err,response){
                if(!response[0]){
                    data={
                        success: false
                    }
                    res.status(200).send(data)
                }else{
                    var data=response[0]
                    data={
                        success: true,
                        roll:response[0].roll,
                        name:response[0].name,
                        department:response[0].department
                    }
                    console.log(response[0])
                    res.status(200).send(data)
                }
            })
        }
    }) 
    
    //attendence collection
    app.post('/search',(req,res)=>{
        let userData=req.body;

        //if(userData.length()!=0){
            attendance.find(userData).toArray(function(err,response){
                if(!response[0]){
                    var data={success:false};
                    res.status(200).send(data);
                }else{
                    res.status(200).send(response);
                    console.log(response);
                }
            })
        //}
    })
})

//server port
app.listen(PORT,function(req,res){
    console.log('Server is running on port: '+PORT);
});
