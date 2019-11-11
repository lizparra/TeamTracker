import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, TextInput, Button, FlatList, ActivityIndicator } from 'react-native';
import { Constants } from 'react-native-unimodules';

const Loading = () => {
    return (
        <View styles={styles.container}>
            <Text>Loading...</Text>
            <ActivityIndicator size="large"/>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    }
});

export default Loading;