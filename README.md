# kotlin-react-descriptive-state
a kotlin react functional component wrapper that enforces user friendly explanations for the current state

## descriptive state
how many times have you encountered the following UX issue? you want to click a button but it's greyed out and you don't know why.

descriptive state solves this by enforcing that state changes are given user friendly descriptions so that the user can hover over the element and will see a description explaining why it's in that state

## example

```kotlin
val foo = descriptiveFunctionalComponent<RProps> {
    val (isEnabled, setIsEnabled) = useState(true, "the button is enabled by defualt")
    return@descriptiveFunctionalComponent functionalComponent<RProps> {
        button {
            attrs {
                disabled = !isEnabled
                onClickFunction = {
                    setIsEnabled(false, "you disabled the button")
                }
            }
            +"disable button"
        }
    }
}
```

## disclaimer
this project is just a POC for an idea i had to improve user experience in GUIs. it hasnt been tested much and i dont know how well it'll scale with an actual project