// Prevent compiler errors when using jQuery.  "$" will be given a type of
// "any", so that we can use it anywhere, and assume it has any fields or
// methods, without the compiler producing an error.
var $: any;
var StoredId: any[] = [];
// The 'this' keyword does not behave in JavaScript/TypeScript like it does in
// Java.  Since there is only one NewEntryForm, we will save it to a global, so
// that we can reference it from methods of the NewEntryForm in situations where
// 'this' won't work correctly.
var newEntryForm: NewEntryForm;

// This constant indicates the path to our backend server (change to your own)
// const backendUrl = "https://desolate-bastion-93856.herokuapp.com/";
const backendUrl = "https://cse216-fl22-team14.herokuapp.com/";

/**
 * NewEntryForm encapsulates all of the code for the form for adding an entry
 */
class NewEntryForm {
  /**
   * To initialize the object, we say what method of NewEntryForm should be
   * run in response to each of the form's buttons being clicked.
   */
  constructor() {
    document.getElementById("addCancel")?.addEventListener("click", (e) => {
      newEntryForm.clearForm();
    });
    document.getElementById("addButton")?.addEventListener("click", (e) => {
      newEntryForm.submitForm();
    });
  }

  /**
   * Clear the form's input fields
   */
  clearForm() {
    (<HTMLInputElement>document.getElementById("newTitle")).value = "";
    (<HTMLInputElement>document.getElementById("newMessage")).value = "";
  }

  /**
   * Check if the input fields are both valid, and if so, do an AJAX call.
   */
  submitForm() {
    window.alert("Submit form called.");
    // get the values of the two fields, force them to be strings, and check
    // that neither is empty
    let title =
      "" + (<HTMLInputElement>document.getElementById("newTitle")).value;
    let msg =
      "" + (<HTMLInputElement>document.getElementById("newMessage")).value;
    if (title === "" || msg === "") {
      window.alert("Error: title or message is not valid");
      return;
    }

    // set up an AJAX POST.
    // When the server replies, the result will go to onSubmitResponse
    const doAjax = async () => {
      await fetch(`${backendUrl}/ideas`, {
        method: "POST",
        body: JSON.stringify({
          mTitle: title,
          mMessage: msg,
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {
          // If we get an "ok" message, return the json
          if (response.ok) {
            return Promise.resolve(response.json());
          }
          // Otherwise, handle server errors with a detailed popup message
          else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {
          newEntryForm.onSubmitResponse(data);
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };

    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);
  }

  /**
   * onSubmitResponse runs when the AJAX call in submitForm() returns a
   * result.
   *
   * @param data The object returned by the server
   */
  private onSubmitResponse(data: any) {
    // If we get an "ok" message, clear the form
    if (data.mStatus === "ok") {
      newEntryForm.clearForm();
      mainList.refresh();
    }
    // Handle explicit errors with a detailed popup message
    else if (data.mStatus === "error") {
      window.alert("The server replied with an error:\n" + data.mMessage);
    }
    // Handle other errors with a less-detailed popup message
    else {
      window.alert("Unspecified error");
    }
  }
} // end class NewEntryForm

// a global for the main ElementList of the program.  See newEntryForm for
// explanation
var mainList: ElementList;

/**
 * ElementList provides a way of seeing all of the data stored on the server.
 */
class ElementList {
  /**
   * refresh is the public method for updating messageList
   */
  refresh() {

    // Issue an AJAX GET and then pass the result to update().
    const doAjax = async () => {
      await fetch(`${backendUrl}/ideas`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {
          // If we get an "ok" message, clear the form
          if (response.ok) {
            return Promise.resolve(response.json());
          }
          // Otherwise, handle server errors with a detailed popup message
          else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {

          mainList.update(data);
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });


    };

    /**
     * attempted to see if i could implement the GET request
     * through the refresh() function so that we are able to see
     * the like count next to the like button
     * However, I couldn't figure out how to implement it because
     * I didn't know how to define the 'id'.
     * 
     * The way I will have it work is based on the click event
     * of the like button 
     * As soon as you click the like button, the like count will be
     * displayed but unfortunately it will only be for an individual 
     * cell/row instead of the entire row of the table
     */


    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);

  }

/**
 * 
 * @param data Data passed from /ideas GET request
 */
  private update(data: any) {


    let elem_messageList = document.getElementById("messageList");

    if (elem_messageList !== null) {
      elem_messageList.innerHTML = "";

      let fragment = document.createDocumentFragment();

      let table = document.createElement("table");

      for (let i = 0; i < data.mData.length; ++i) {
        let tr = document.createElement("tr");
        let td_title = document.createElement("td");
        let td_massage = document.createElement("td");
        // let td_likeid = document.createElement("td");
        td_title.innerHTML = data.mData[i].title;
        td_massage.innerHTML = data.mData[i].massage;
        // td_likeid.innerHTML = data.mData[i].idea_id;
        tr.appendChild(td_title);
        tr.appendChild(td_massage);
        tr.appendChild(this.buttons(data.mData[i].id));
        // tr.appendChild(td_likeid);

        table.appendChild(tr);
      }

      fragment.appendChild(table);

      elem_messageList.appendChild(fragment);
    }
    // Find all of the delete buttons, and set their behavior
    const all_delbtns = <HTMLCollectionOf<HTMLInputElement>>(
      document.getElementsByClassName("delbtn")
    );
    for (let i = 0; i < all_delbtns.length; ++i) {
      all_delbtns[i].addEventListener("click", (e) => {
        mainList.clickDelete(e);
      });
    }
    // Find all of the edit buttons, and set their behavior
    const all_editbtns = <HTMLCollectionOf<HTMLInputElement>>(
      document.getElementsByClassName("editbtn")
    );
    for (let i = 0; i < all_editbtns.length; ++i) {
      all_editbtns[i].addEventListener("click", (e) => {
        mainList.clickEdit(e);
      });
    }
    //adding in a Like button
    const all_likebtns = <HTMLCollectionOf<HTMLInputElement>>(
      document.getElementsByClassName("likebtn")
    );

    for (let i = 0; i < all_likebtns.length; ++i) {
      // all_likebtns[i]
      let clickCounter = 0;
      all_likebtns[i].addEventListener("click", (e) => {
        //get
        mainList.clickLike(e);


        clickCounter++;
        if (clickCounter === 1) {
          mainList.clickLikePost(e, i);
        }
        else if (clickCounter === 2) {
          mainList.clickLikeDelete(e, i);

          clickCounter = 0;
        }
        // console.log("Button has been clicked: ", clickCounter);

        mainList.clickLike(e);

      });
    }
  }

  /**
   * clickDelete is the code we run in response to a click of a delete button
   */
  private clickDelete(e: Event) {
    const id = (<HTMLElement>e.target).getAttribute("data-value");

    // Issue an AJAX DELETE and then invoke refresh()
    const doAjax = async () => {
      await fetch(`${backendUrl}/ideas/${id}`, {
        method: "DELETE",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {
          if (response.ok) {
            return Promise.resolve(response.json());
          } else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {
          mainList.refresh();
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };

    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);

    // TODO: we've reapeated the same pattern 3+ times now, so we should really
    //       think about refactoring and abstracting this boilerplate into something
    //       easier to reuse, if possible
  }
  /**
   * clickEdit is the code we run in response to a click of a edit button
   */
  private clickEdit(e: Event) {
    // as in clickDelete, we need the ID of the row
    const id = (<HTMLElement>e.target).getAttribute("data-value");

    // Issue an AJAX GET and then pass the result to editEntryForm.init()
    const doAjax = async () => {
      await fetch(`${backendUrl}/ideas/${id}`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {
          if (response.ok) {
            return Promise.resolve(response.json());
          } else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {
          editEntryForm.init(data);
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };

    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);
  }
  /**
   * Purpose:
   * Do a GET request when clicking the like button and to display the like count
   * 
   * @param e The on click event
   */
  private clickLike(e: Event) {
    // as in clickLike, we need the ID of the row
    const id = (<HTMLElement>e.target).getAttribute("data-value");

    // Issue an AJAX POST and then pass the result to
    const doAjax = async () => {
      await fetch(`${backendUrl}/likes/${id}`, {
        method: "GET",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {

          if (response.ok) {
            return Promise.resolve(response.json());
          } else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {


          console.log("Data: ", data.mData);


          let LikeList = document.getElementById("likeList");
          if (LikeList !== null) {
            LikeList.innerHTML = data.mData;

          }

          console.log("DATA: ", data);

          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };

    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);
  }
  /**
   * Purpose: 
   * To do a POST request for liking a post
   * 
   * @param e  The on click event
   * @param value The specific post whose liked is to be posted
   */
  private clickLikePost(e: Event, value: number) {
    // as in clickLike, we need the ID of the row
    const id = (<HTMLElement>e.target).getAttribute("data-value");

    // Issue an AJAX POST and then pass the result to
    const doAjax = async () => {
      await fetch(`${backendUrl}/likes/${id}`, {
        method: "POST",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {

          if (response.ok) {
            return Promise.resolve(response.json());
          } else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {

          const getLikeBtnFunc = <HTMLCollectionOf<HTMLInputElement>>(document.getElementsByClassName("likebtn"));
          getLikeBtnFunc[value].style.background = "lightgreen";

          // editEntryForm.init(data);
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };

    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);
  }
  /***
   * Purpose: 
   * DELETE request for removing a like from a post/idea
   * 
   * @param e The onclick event
   * @param value The specific post whose liked is to be removed
   */
  private clickLikeDelete(e: Event, value: number) {
    // as in clickLike, we need the ID of the row
    const id = (<HTMLElement>e.target).getAttribute("data-value");

    // Issue an AJAX POST and then pass the result to
    const doAjax = async () => {
      await fetch(`${backendUrl}/likes/${id}`, {
        method: "DELETE",
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {

          if (response.ok) {

            return Promise.resolve(response.json());
          } else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          return Promise.reject(response);
        })
        .then((data) => {
          const getLikeBtnFunc = <HTMLCollectionOf<HTMLInputElement>>(document.getElementsByClassName("likebtn"));
          getLikeBtnFunc[value].style.background = "white";
          // editEntryForm.init(data);
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };

    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);
  }


  /**
   * buttons() adds a 'delete' button and an 'edit' button to the HTML for each row
   */
  private buttons(id: string): DocumentFragment {
    let fragment = document.createDocumentFragment();
    let td = document.createElement("td");

    // create edit button, add to new td, add td to returned fragment
    let btn = document.createElement("button");
    btn.classList.add("editbtn");
    btn.setAttribute("data-value", id);
    btn.innerHTML = "Edit";
    td.appendChild(btn);
    fragment.appendChild(td);

    // create delete button, add to new td, add td to returned fragment
    td = document.createElement("td");
    btn = document.createElement("button");
    btn.classList.add("delbtn");
    btn.setAttribute("data-value", id);
    btn.innerHTML = "Delete";
    td.appendChild(btn);
    fragment.appendChild(td);

    //create like button, add to new td, add td to returned fragment
    td = document.createElement("td");
    btn = document.createElement("button");
    btn.classList.add("likebtn");
    btn.setAttribute("data-value", id);
    btn.innerHTML = "Like";

    // btn.addEventListener('click', clickLike)
    td.appendChild(btn);
    fragment.appendChild(td);

    return fragment;
  }
} // end class ElementList

// a global for the EditEntryForm of the program.  See newEntryForm for explanation
var editEntryForm: EditEntryForm;

/**
 * EditEntryForm encapsulates all of the code for the form for editing an entry
 */
class EditEntryForm {
  /**
   * To initialize the object, we say what method of EditEntryForm should be
   * run in response to each of the form's buttons being clicked.
   */
  constructor() {
    document.getElementById("editCancel")?.addEventListener("click", (e) => {
      editEntryForm.clearForm();
    });
    document.getElementById("editButton")?.addEventListener("click", (e) => {
      editEntryForm.submitForm();
    });
  }

  /**
   * init() is called from an AJAX GET, and should populate the form if and
   * only if the GET did not have an error
   */
  init(data: any) {
    // If we get an "ok" message, fill in the edit form
    if (data.mStatus === "ok") {
      // show the edit form
      (<HTMLElement>document.getElementById("editElement")).style.display =
        "block";
      //Like Element
      // (<HTMLElement>document.getElementById("likeElement")).style.display = "block";

      (<HTMLElement>document.getElementById("addElement")).style.display =
        "none";
      (<HTMLElement>document.getElementById("showElements")).style.display =
        "none";

      (<HTMLInputElement>document.getElementById("editTitle")).value =
        data.mData.title;
      (<HTMLInputElement>document.getElementById("editMessage")).value =
        data.mData.massage;
      (<HTMLInputElement>document.getElementById("editId")).value =
        data.mData.id;
      (<HTMLInputElement>document.getElementById("editCreated")).value =
        data.mData.mCreated;
    }
    // Handle explicit errors with a detailed popup message
    else if (data.mStatus === "error") {
      window.alert("The server replied with an error:\n" + data.mMessage);
    }
    // Handle other errors with a less-detailed popup message
    else {
      window.alert("Unspecified error");
    }
  }

  /**
   * Clear the form's input fields
   */
  clearForm() {
    (<HTMLInputElement>document.getElementById("editTitle")).value = "";
    (<HTMLInputElement>document.getElementById("editMessage")).value = "";
    (<HTMLInputElement>document.getElementById("editId")).value = "";
    (<HTMLInputElement>document.getElementById("editCreated")).value = "";
  }

  /**
   * Check if the input fields are both valid, and if so, do an AJAX call.
   */
  submitForm() {
    window.alert("Submit edit form called.");
    // get the values of the two fields, force them to be strings, and check
    // that neither is empty
    let title =
      "" + (<HTMLInputElement>document.getElementById("editTitle")).value;
    let msg =
      "" + (<HTMLInputElement>document.getElementById("editMessage")).value;
    // NB: we assume that the user didn't modify the value of editId
    let id = "" + (<HTMLInputElement>document.getElementById("editId")).value;
    if (title === "" || msg === "" || id === "") {
      window.alert("Error: title, message, or id is not valid");
      return;
    }

    // set up an AJAX PUT.
    // When the server replies, the result will go to onSubmitResponse
    const doAjax = async () => {
      await fetch(`${backendUrl}/ideas/${id}`, {
        method: "PUT",
        body: JSON.stringify({
          mTitle: title,
          mMessage: msg,
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8",
        },
      })
        .then((response) => {
          // If we get an "ok" message, return the json
          if (response.ok) {
            // return response.json();
            return Promise.resolve(response.json());
          }
          // Otherwise, handle server errors with a detailed popup message
          else {
            window.alert(
              `The server replied not ok: ${response.status}\n` +
              response.statusText
            );
          }
          // return response;
          return Promise.reject(response);
        })
        .then((data) => {
          editEntryForm.onSubmitResponse(data);
          console.log(data);
        })
        .catch((error) => {
          console.warn("Something went wrong.", error);
          window.alert("Unspecified error");
        });
    };

    // make the AJAX post and output value or error message to console
    doAjax().then(console.log).catch(console.log);
  }

  /**
   * onSubmitResponse runs when the AJAX call in submitForm() returns a
   * result.
   *
   * @param data The object returned by the server
   */
  private onSubmitResponse(data: any) {
    // If we get an "ok" message, clear the form and refresh the main
    // listing of ideas
    if (data.mStatus === "ok") {
      editEntryForm.clearForm();
      mainList.refresh();
    }
    // Handle explicit errors with a detailed popup message
    else if (data.mStatus === "error") {
      window.alert("The server replied with an error:\n" + data.mMessage);
    }
    // Handle other errors with a less-detailed popup message
    else {
      window.alert("Unspecified error");
    }
  }
} // end class EditEntryForm

// Run some configuration code when the web page loads
document.addEventListener(
  "DOMContentLoaded",
  () => {
    // set up initial UI state
    (<HTMLElement>document.getElementById("editElement")).style.display =
      "none";
    //like element
    // (<HTMLElement>document.getElementById("likeElement")).style.display = "none";
    // add click event for when the post is liked

    (<HTMLElement>document.getElementById("addElement")).style.display = "none";
    (<HTMLElement>document.getElementById("showElements")).style.display =
      "block";
    // set up the "Add Message" button
    document
      .getElementById("showFormButton")
      ?.addEventListener("click", (e) => {
        (<HTMLElement>document.getElementById("addElement")).style.display =
          "block";
        (<HTMLElement>document.getElementById("showElements")).style.display =
          "none";
      });
    // reset the UI
    (<HTMLElement>document.getElementById("editElement")).style.display =
      "none";
    // (<HTMLElement>document.getElementById("likeElement")).style.display = "none";

    (<HTMLElement>document.getElementById("addElement")).style.display = "none";
    (<HTMLElement>document.getElementById("showElements")).style.display =
      "block";

    // Create the object that controls the "Edit Entry" form
    editEntryForm = new EditEntryForm();
    // Create the object that controls the "New Entry" form
    newEntryForm = new NewEntryForm();
    // Create the object for the main data list, and populate it with data from the server
    mainList = new ElementList();
    mainList.refresh();

    window.alert("DOMContentLoaded");
  },
  false
);
