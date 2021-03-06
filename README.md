expense
=======

Simple command line tool to manage daily expenses.

Usage
-----

To add an expense of amount 500:

```
$ x -a 500 "some test expense"
```

To add an expense for some previous date, say 3rd of the current month:

```
$ x -d 3 -a 500 "some test expense"
```

To add an expense for a given date such as 3rd May 2016:

```
$ x -d 3 -m 5 -y 2016 -a 500 "some test expense"
```

To view the total for the current month:

```
$ x -t
```

To view the total for a previous month

```
$ x -m 3 -t
```

To sort all expenses in store, chronologically:

```
$ x -s
```

Versioning
----------

For transparency and insight into our release cycle, and for striving to maintain backward compatibility,
`expense` will be maintained under the Semantic Versioning guidelines as much as possible.

Releases will be numbered with the follow format:

	<major>.<minor>.<patch>

And constructed with the following guidelines:

* Breaking backward compatibility bumps the major
* New additions without breaking backward compatibility bumps the minor
* Bug fixes and misc changes bump the patch

For more information on SemVer, please visit http://semver.org/.

License
-------

```
expense - CLI for managing daily expenses
Copyright (c) 2015, Sandeep Gupta

	http://sangupta.com/projects/expense

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
