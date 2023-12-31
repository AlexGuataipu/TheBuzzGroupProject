import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_application_1/api/google_signin_api.dart';
import 'package:flutter_application_1/page/view_idea.dart';

import '../main.dart';
import 'create_post_page.dart';

import '../objects/user.dart';

// Profile Page for viewing other users profile information
// Displays: username, profile picture, name, note
class PublicProfilePage extends StatefulWidget {
  final DBUser user;
  final User currentUser;

  const PublicProfilePage({
    Key? key,
    required this.user,
    required this.currentUser,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => PublicProfileState();
}

class PublicProfileState extends State<PublicProfilePage> {
  

  int selectedIndex = 1;
  
  get http => null; // Navigation bar, shows profile is selected
  
  // This method will make it so the bottom navigation bar works and highlights
  // whatever tab ur supposed to be in
  void itemTapped(int index) {
    setState(() {
      selectedIndex = index;
      if (selectedIndex == 0) { // When home icon is selected, push HomePage
        Navigator.push(
            context,
            MaterialPageRoute(builder: (context) => MyHomePage(title: 'The Buzz', user: widget.currentUser)),
          );
      } 
    });
  }

  @override
  Widget build(BuildContext context) => Scaffold(
    appBar: AppBar(
      title: const Text('The Buzz'),
      centerTitle: true,
      actions: [
        // *** Can't implement without sending all the necessary data through to the page
        //
        // ElevatedButton.icon( // Logout button function
        //   label: const Text('Back'),
        //   icon: const Icon(Icons.arrow_back),
        //   onPressed: () async {
        //     Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => ViewIdeaPage(title: 'The Buzz', user: widget.currentUser, id: null, idea: null,),
        //     ));
        //   },
        // )
      ],
    ),
    body: Container(
      alignment: Alignment.topLeft,
      color: Colors.white,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        // Layout of profile page
        children: [
          const SizedBox(height: 15),
          Center( // Profile Picture
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              children: [
                const SizedBox(width: 5),
                const Expanded (
                  flex: 0,
                  child: CircleAvatar(
                    radius: 45,
                    backgroundColor: Color.fromARGB(255, 251, 207, 126),
                  ),
                ),
                // Expanded(
                //   child: const SizedBox(width: 10)
                // ),
                Expanded (
                  flex: 2,
                  child: Column(
                    children: [
                      Container (
                        width: 250,
                        height: 20,
                        alignment: Alignment.centerLeft,
                        child: Text(
                        ' ${widget.user.displayName}',
                        style: const TextStyle(color: Colors.black, fontSize: 20, ),
                        textAlign: TextAlign.left,
                        ),
                      ),
                      Container (
                        width: 250,
                        height: 25,
                        alignment: Alignment.centerLeft,
                        child: Text(
                        ' @${((widget.user.email!).split('@')[0])}',
                        style: const TextStyle(color: Color.fromARGB(200, 233, 30, 98), fontSize: 20,),
                        textAlign: TextAlign.left,
                        ),
                      ),
                    ]
                  ),
                ),
                const SizedBox(width: 7),
              ],
            ),
          ),
          const SizedBox(height: 10),
          // User short description
          Container (
            width: 375,
            height: 35,
            alignment: Alignment.centerLeft,
            child: Text(
            ' ${((widget.user.displayName!).split(' ')[0])}\'s Note',
            style: const TextStyle(color: Colors.black, fontSize: 18,),
            textAlign: TextAlign.left,
            ),
          ),
          Container (
            alignment: Alignment.topLeft,
            width: 375,
            height: 150,
            decoration: BoxDecoration(
              color: const Color.fromARGB(255, 251, 207, 126),
              border: Border.all(
                color: const Color.fromARGB(150, 233, 30, 98),
                width: 1.5,
              ),
              borderRadius: BorderRadius.circular(5),
              ),    
            child: const Text(
              ' N/A',
              style: TextStyle(color: Colors.black, fontSize: 18,),
              textAlign: TextAlign.left,
            ),
          ),
        ],
      ),
    ),
  );
}
