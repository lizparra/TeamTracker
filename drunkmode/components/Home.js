import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, TextInput, Button, FlatList, TouchableOpacity } from 'react-native';
import { Constants } from 'react-native-unimodules';
import firebase from 'firebase';

class Home extends Component {
     state = {
         email: '',
         displayName: ''
     }

     componentDidMount(){
         const { email, displayName } = firebase.auth().currentUser;

         this.setState({ email, displayName });
     }

     render(){
         return (
             <View styles={styles.container}>
                 <View style={styles.articleContainer}>
                     <Text style={styles.heading}>
                         Welcome to the authorized screen {this.state.email}!
                     </Text>

                     <Text style={styles.content}>
                         You're are logged in from Firebase
                     </Text>

                 </View>

                 <TouchableOpacity style={{padding: 20}} onPress={() => firebase.auth().signOut()}>
                      <Text style={{color: '#1B9CFC'}}>
                          Log Out
                      </Text>
                  </TouchableOpacity>

             </View>
         );
     }
 }

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    articlesContainer: {
        padding: 10,
        borderBottomColor: 'rgba(255,255,255,.7)',
        borderBottomWidth: 5,

    },
    heading: {
        fontSize: 22,
        color: 'black',
        marginBottom: 10
    },
    content: {
        marginTop: 10,
        fontSize: 19
    }
});

export default Home;