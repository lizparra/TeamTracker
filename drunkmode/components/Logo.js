import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, TextInput, Button, FlatList, Image } from 'react-native';
import { Constants } from 'react-native-unimodules';

const Logo = () => {
    return (
        <View>
            <Image source={require('./images/nausea-icon-19.jpg')} style={{height: 150, width: 150, marginTop: 50}}/>
        </View>
    );
}

export default Logo;