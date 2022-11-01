import 'package:flutter/material.dart';
import 'package:google_sign_in/google_sign_in.dart';
import 'dart:developer' as developer;
import 'dart:convert';
import 'package:http/http.dart' as http;

import 'profile_page.dart';
import 'create_post_page.dart';
import 'login_page.dart';
import '../main.dart';

import '../objects/user.dart';

class ViewIdeaPage extends StatefulWidget {
  final User user;
  final Idea idea;
  final int id;

  const ViewIdeaPage({
    Key? key,
    required this.user,
    required this.idea,
    required this.id,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => ViewIdeaState();
}

class ViewIdeaState extends State<ViewIdeaPage> {
  final commentController = TextEditingController();
      
  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    commentController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('The Buzz'),
        centerTitle: true,
        actions: [
          ElevatedButton.icon(
            label: const Text('Home'),
            icon: const Icon(Icons.home),
            onPressed: () {
              Navigator.of(context).pushReplacement(MaterialPageRoute(builder: (context) => MyHomePage(user: widget.user, title: 'The Buzz'),
              ));
            },
          )
        ],
      ),
      body: 
        Padding(
          padding: const EdgeInsets.fromLTRB(20, 10, 20, 10),
          child: Column(
            children: [
            Container(
            ),
            Container(
              alignment: Alignment.topLeft,
              width: 400,
              height: 150,
              decoration: BoxDecoration(
                color: Color.fromARGB(176, 251, 207, 126),
                border: Border.all(
                  color: const Color.fromARGB(150, 233, 30, 98),
                  width: 1.5,
                ),
                borderRadius: BorderRadius.circular(3),
                ),    
              child: Column(
                crossAxisAlignment: CrossAxisAlignment. start,
                children: [
                  Text(
                    '${widget.idea.userid}',
                    style: TextStyle(color: Colors.black, fontSize: 18,),
                    textAlign: TextAlign.left,
                  ),
                  const SizedBox(height: 7),
                  Text(
                    textAlign: TextAlign.left,
                    '${widget.idea.title}',
                    style: TextStyle(color: Colors.black, fontSize: 20, fontWeight: FontWeight.bold,),
                  ),
                  const SizedBox(height: 5),
                  Text(
                    textAlign: TextAlign.left,
                    '  ${widget.idea.message}',
                    style: TextStyle(color: Colors.black, fontSize: 16,),
                  ),
                ],
              ),
            ),
            SizedBox(height: 3),
            Container(
              decoration: BoxDecoration(
                color: Color.fromARGB(255, 255, 255, 255),
                border: Border.all(
                  color: const Color.fromARGB(150, 233, 30, 98),
                  width: 1.5,
                ),
                borderRadius: BorderRadius.circular(3),
              ),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Container(
                    alignment: Alignment.topLeft,
                    child: Text(
                      ' @${widget.user.uid}'
                    ),
                  ),
                  SizedBox(width: 150),
                                Container(
                                  alignment: Alignment.centerRight,
                                  child: IconButton(
                                    color: Colors.pink, 
                                    onPressed: () { 
                                      setState(() {
                                        //pass in snapshot.data.id
                                        //postLiked[i] = !postLiked[i];
                                    });
                                    }, 
                                    icon: const Icon(Icons.favorite_outline),
                                  ),
                                ),
                                Container(
                                  alignment: Alignment.centerRight,
                                  child: IconButton(
                                    color: Colors.blue, 
                                    onPressed: () { 
                                      setState(() {
                                        //pass in snapshot.data.id
                                        // postDisliked[i] = !postDisliked[i];
                                        // postLiked[i] = !postLiked[i];
                                    });
                                    }, 
                                    icon: const Icon(Icons.heart_broken_outlined),
                                  ),
                                ),
                                // Container(
                                //   alignment: Alignment.centerRight,
                                //   child: IconButton(
                                //     color: Colors.blue, 
                                //     onPressed: () { 
                                //       setState(() {
                                //         addComment(String);
                                //     });
                                //     }, 
                                //     icon: const Icon(Icons.comment),
                                //   ),
                                // )
                ],
              ),
            ),
            const SizedBox(height: 2),
            Container(
              child: TextField(
                controller: commentController,
                maxLength: 50,
                keyboardType: TextInputType.multiline, //Expands text and creates a new line
                minLines: 1,
                maxLines: 3,
                decoration: const InputDecoration(
                    hintText: 'Add Comment', border: OutlineInputBorder()),
              ),
            ),
            ElevatedButton.icon(
                style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.pink,
                      foregroundColor: Colors.white,
                      fixedSize: const Size(200, 80), 
                    ),
                onPressed: () {
                    // Send user back to home page, ideas displayed up-to-date
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => MyHomePage(title: 'The Buzz', user: widget.user)),
                  );
                  setState(() {
                    addComment(commentController.text, widget.id, widget.user);
                  });
                },
                icon: Icon(Icons.comment),
                label: const Text(
                  'Add Comment',
                  style: TextStyle(
                    fontSize: 20
                  ),
                )
              ),
            ],
          ),
        ),
    );
  }
  
  addComment(String comment, int id, User user) async {
    final response = await http.post(
      Uri.parse('https://cse216-fl22-team14.herokuapp.com/ideas/$id/comment?sessionKey=${user.sessionKey}'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(<String, String>{
        'mUserId': widget.user.id!,
        'mContent': comment,
      }),
    );
    if (response.statusCode == 200) {
      // If the server did return a 200 CREATED response,
      // then parse the JSON.
    } else {
      // If the server did not return a 200 CREATED response,
      // then throw an exception.
      throw Exception('Failed to create Post.');
    }
  }
}