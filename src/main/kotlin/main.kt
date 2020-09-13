import kotlinx.html.*
import react.*
import react.dom.span

/** a [FunctionalComponent] that uses [DescriptiveRBuilder.useState] */
typealias DescriptiveFunctionalComponent<p> = DescriptiveRBuilder<p>.(props: p) -> FunctionalComponent<p>

/**
 * creates a [DescriptiveRBuilder]
 * @return the [FunctionalComponent] created by the [DescriptiveRBuilder]
 */
fun <p : RProps> descriptiveFunctionalComponent(func: DescriptiveFunctionalComponent<p>) =
    DescriptiveRBuilder(func).component

/** an [RBuilder] that enforces user friendly descriptions of state changes */
class DescriptiveRBuilder<p : RProps>(func: DescriptiveFunctionalComponent<p>) : RBuilder() {
    /** a wrapper component that wraps the [FunctionalComponent] in a [span] with the [stateDescriptionTooltip] as the [CommonAttributeGroupFacade.title] */
    var component: FunctionalComponent<p>
    private var stateDescriptionTooltip: String? = null
    private lateinit var setStateDescriptionTooltip: RSetState<String>

    init {
        component = functionalComponent {
            val (stateDescriptionTooltip, setStateDescriptionTooltip) = react.useState<String?>(null)
            this@DescriptiveRBuilder.stateDescriptionTooltip = stateDescriptionTooltip
            this@DescriptiveRBuilder.setStateDescriptionTooltip = setStateDescriptionTooltip
            span {
                attrs {
                    stateDescriptionTooltip?.let { title = it }
                }
                child(this@DescriptiveRBuilder.func(it))
            }
        }
    }

    /** a wrapper for the [react.useState] with a required [stateChangeDescription] which is applied to the [stateDescriptionTooltip] */
    fun <T> useState(initValue: T, stateChangeDescription: String): Pair<T, (value: T, valueReason: String) -> Unit> {
        val (getState, setState) = react.useState(initValue)
        if (this.stateDescriptionTooltip == null)
            this.setStateDescriptionTooltip(stateChangeDescription) //initial value description
        val setStateWithReason = { newValue: T, reason: String ->
            setState(newValue)
            this.setStateDescriptionTooltip(reason) //subsequent value descriptions
        }
        return getState to setStateWithReason
    }

    /** prevents [react.useState] from being used within a [DescriptiveRBuilder] */
    @Suppress("unused_parameter")
    fun <T> useState(initValue: T): Nothing = error("you must specify a stateChangeDescription for state changes in DescriptiveFunctionalComponents")
}