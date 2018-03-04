# Prototype code inMobiles 

This is a prototype for an Android application inculding the following:
A call to an exeternal API, content adapting and storing in a SQlite database, and data display.
(Retrofit2, ContentProvider, RecyclerView) in use.

## Run the application

To run the application, clone this repository and import that project in Android Studio.

Compile-related code is not included in this repository.

## Source code

You can find the code in app / java / com.something.patrick.inmobiles

Item.java represents the item model

ItemsProvider.java is the ContentProvider a middle layer sitting between the database/API and the application interface

Api.java responsible for abstracting the calls to the external API

Adapter.java adapts the data to display

MainActivity the bootstrapping class


