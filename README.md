# ClojureScript demo application

> "Clojure rocks. JavaScript reaches."
>
> "Programming isn't about typing, it's about thinking."

## What

A single-page, data-driven ClojureScript web app which shows a daily schedule
of shows on four of the principal Icelandic TV stations.

A live demo can be found [here][demo].

In this learning session we will consider:

* JavaScript as a **runtime**
* [ClojureScript][] as a **state management** solution
* [Figwheel][] as a tool for **live programming**
* [React][] as an **optimized HTML rendering engine**
* [clojure.spec][spec] for **programming by contract**
* [core.async][] as a tool for facilitating **asynchronous programming**
* [Rum][] as a convenient API to tie everything together

## Why

What do ClojureScript and Rum give us that JavaScript or other
compile-to-JavaScript languages don't?

[ClojureScript][] is primarily of interest when you have already bought into
Clojure. It perhaps makes less sense if you haven't or don't intend to. In this
case, you may get more mileage from [Elm][] or even [PureScript][].

Since Akvo already has an investment in Clojure, ClojureScript could be very
attractive in that it allows the use of one language throughout the web
application stack. [Rum][] also supports server-side component rendering.

Clojure, unlike JavaScript, has a **stable** and **concise** syntax which is not
subject to change or interpretation.

A recent enhancement to Clojure, [clojure.spec][spec] is a pragmatic solution to
the problem of type safety. See [README_SPEC](README_SPEC.md) for more
information on this exciting topic.

[React][] is rapidly becoming the toolkit of choice for modern web developers
and is developed by a huge corporation with unlimited development resources.
It can be seen as an industry-standard rendering engine ClojureScript gets
for free.

[core.async][] is, essentially, a code-rewriting macro that facilitates
asynchronous programming. It is nothing "magical" but, rather, provides a
Clojure-idiomatic way of making asynchronous code much easier to think about
and write. Think of it, perhaps, as a way of escaping [Callback Hell][].

No Node.js. No NPM. No Bower. No Gulp. No Grunt. No Webpack. No Browserify.
No Brunch. No Yeoman. No Uglify. Relax! \o/

## How

### Prerequisites

A basic grasp of Clojure code & data structures is useful (but not essential)
for this session. JavaScript domain knowledge will also be beneficial.

To build this app yourself, you will need to have a recent [JVM][] installed
and the [Clojure][] build tool [Leiningen][].

### Build and run

Once everything is installed, type the following to create an optimized,
production build of the app:

```shell
lein release
```

Now open `resources/public/index.html` in your web browser.

### Live ClojureScript development

You can also run the app in development using [Figwheel][] as follows:

```shell
lein figwheel
```

This command will also pop open a browser window running the application.

Note that some unit tests are included and will run in the browser console
every time the application code is saved. Tests can be found in the
[`tv.tests`][test/cljs/tv/tests.cljs] namespace.

### Live SCSS development

Enter the following command in a separate terminal to watch and compile
the [SCSS][] styles found in [`src/scss/styles.scss`][src/scss/styles.css].

```shell
lein sass
```

## Who

This app was written by [Paul Burt][pb].

But **is it any good?**

[Yes][].

## Further information

[ClojureScript for Skeptics] is worth a watch if you're not (yet) convinced by
ClojureScript.

[callback hell]: http://callbackhell.com/
[clojure]: http://clojure.org/
[clojurescript]: https://clojurescript.org/
[core.async]: https://github.com/clojure/core.async
[demo]: https://public-vjhonmvsgw.now.sh/#/ruv
[elm]: http://elm-lang.org/
[figwheel]: https://github.com/bhauman/lein-figwheel
[jvm]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[leiningen]: http://leiningen.org/
[pb]: https://twitter.com/pycurious
[purescript]: http://www.purescript.org/
[react]: https://github.com/facebook/react
[rum]: https://github.com/tonsky/rum
[clojurescript for skeptics]: https://www.youtube.com/watch?v=gsffg5xxFQI
[spec]: http://clojure.org/about/spec
[yes]: https://news.ycombinator.com/item?id=3067434
[scss]: http://sass-lang.com/
