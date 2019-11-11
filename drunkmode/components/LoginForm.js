import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, TextInput, Button, FlatList } from 'react-native';
import { Constants } from 'react-native-unimodules';
import Logo from './Logo';
import Login from './Login';

class LoginForm extends Component {
    render(){
        return (
            <View styles={styles.container}>
                <View style={styles.logoContainer}>
                    <Logo/>
                </View>
                <Text style={styles.title}>Drunk Mode</Text>
                <View style={styles.Login}>
                    <Login/>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        marginTop: Constants.statusBarHeight
    },
    logoContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    Login: {
        flex: 2
    },
    title: {
        fontWeight: 'bold',
        fontSize: 40,
        color: 'black',
        textAlign: 'center',
        margin: 40
    }
});

export default LoginForm;