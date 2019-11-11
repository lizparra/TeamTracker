import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, TextInput, Button, FlatList, ImageBackground } from 'react-native';
import { Constants } from 'react-native-unimodules';
import LoginForm from './LoginForm';
import Home from './Home';
import BG from './images/app-background-white.jpg';
import firebase from 'firebase';
import Loading from './Loading';
//import { createAppContainer } from 'react-navigation';
//import { createStackNavigator } from 'react-navigation-stack';

class App extends Component{

//    const MainNavigator = createStackNavigator({
//        Login: {screen: Login},
//        RegisterForm: {screen: RegisterForm},
//    });

    state = {
        loggedIn: null
    }

    componentDidMount(){
        var firebaseConfig = {
            apiKey: "AIzaSyAjjFZlI1oBd7KMAfoggrzZe2AoM6Xtbj8",
            authDomain: "drunkmode-e0709.firebaseapp.com",
            databaseURL: "https://drunkmode-e0709.firebaseio.com",
            projectId: "drunkmode-e0709",
            storageBucket: "drunkmode-e0709.appspot.com",
            messagingSenderId: "76602159439",
            appId: "1:76602159439:web:24c2369b32aa1bc97f153a",
            measurementId: "G-TV0D9P4W98"
        };

        if (!firebase.apps.length) {
            firebase.initializeApp(firebaseConfig);
        }

        firebase.auth().onAuthStateChanged(user => {
            if(user){
                this.setState({
                    loggedIn: true
                })
            } else {
                this.setState({
                    loggedIn: false
                })
            }
        })
    }

    renderContent = () => {
        switch(this.state.loggedIn){
            case false:
                return <ImageBackground style={styles.container} source={BG}>
                            <LoginForm/>
                        </ImageBackground>

            case true:
                return <Home/>

                default:
                    return <Loading/>
        }
    }

    render(){
        return (
            <View style={styles.container}>
                { this.renderContent() }
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        height: '100%',
        width: '100%'
    }
});

export default App;