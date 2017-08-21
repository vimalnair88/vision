Vision
===================


A wearable assistant for the visually impaired

----------


Abstract
-------------

We are proposing a solution that will encompass multiple capabilities using a single device. The ability to provide a descriptive narrative of the real world is the need of the hour. In this project, we aim to harness the potential of image recognition and deep learning techniques to classify, categorize and describe visual scenarios to blind people via audio and speech. Our project “Vision”, would primarily be equipped with hardware modules like a smartphone (any device capable of processing the information) and a camera, which would act as the eye for the visually impaired and can be mounted on a pendant or a hat. It would also consist of a microphone, which will take voice input requests from the user and a pair of headphones, which present the audio output as response and instructions to the user. As the user focuses the camera on a view, the information is captured by the smartphone/raspberry pi and then transferred to the algorithm to recognize the view as a ‘plain text or image’, which may have extended to a surrounding or a scenario. Apart from providing the blind with a sophisticated alternative to interact with their surroundings, we believe such a system help them appreciate life more, aid their independence and provide them with opportunities that are currently not feasible.



----------


Project Architecture
-------------------

#### <i></i> Alexa voice Interaction
> The main module of the project is the voice interaction module created using Alexa skills set. The idea is to build a custom skill using the Alexa developer console. Once a custom skill is added, the user can interact with the Amazon Echo using custom questions catered for the ‘Vision Project’. The examples include ‘Alexa Smart Pendant Describe what is in front of me’ which is custom voice skill developed to cater the end user the detailed narrative of the scene in front of him.

#### <i></i> Data Management
> The choice of data management platform is an important design decision considering the factors like Scalability, Highly performant, Consistent performance etc. Amazon Dynamo DB is a NoSQL Database which provides consistent and single digit latency at any scale. It’s an internally managed database which supports both key value stores and document level data. High performance and low latency are key factors which are the main requirements of our project. Another important consideration is the fact that it provides seamless integration with Amazon Lambda which is the main orchestration engine in our project. Dynamo DB data can be fetched when an Amazon Lambda function is triggered by the Alexa voice command.

#### <i></i> Deep Learning Model
> Image recognition and cognitive service are the key components of our project. The accuracy of the information delivered to the end user depends on the effectiveness of the deep learning model used for the prediction of the Image. We are leveraging the API of IBM Watson for image recognition. We can perform a custom training on top of the API provided by IBM Watson so that we can train the model to a greater degree such that the overall prediction accuracy is increased.
#### <i></i> Real Time Data Processing
> Real Time Data Processing in our project is handled by Amazon Lambda function. Its acts as the main orchestration engine between the Amazon Alexa and the Dynamo DB. As soon as the user interacts with Amazon echo using voice command, an Amazon Lambda function is triggered which fetches data from the Dynamo DB and feeds the same to the Alexa custom skill set module. This is in turn converted to voice and send to the end user.

----------

