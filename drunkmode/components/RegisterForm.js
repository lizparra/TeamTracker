import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, TextInput, Button, FlatList } from 'react-native';
import { Constants } from 'react-native-unimodules';
import Logo from './Logo';

class RegisterForm extends Component {
    render(){

        return (
            <View styles={styles.container}>
                <View style={styles.logoContainer}>
                    <Logo/>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center'
    },
    logoContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
});

export default RegisterForm;