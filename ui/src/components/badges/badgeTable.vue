<template>
    <div>
        <div class="row">
            <div class="column">
                <single-badge id="streaker-badge" :source-badge="'streaker'" :achieved-badge="true" :achievement-level="2"></single-badge>
                <single-badge id="wayfarer-badge" :source-badge="'wayfarer'" :achieved-badge="true"></single-badge>
                <single-badge id="cartographer-badge" :source-badge="'cartographer'" :achieved-badge="true"></single-badge>
            </div>
            <div class="column">
                <single-badge id="writer-badge" :source-badge="'writer'" :achieved-badge="true"></single-badge>
                <single-badge id="solver-badge" :source-badge="'solver'" :achieved-badge="true"></single-badge>
                <single-badge id="explorer-badge" :source-badge="'explorer'" :achieved-badge="false"></single-badge>
            </div>
            <div class="column">
                <single-badge id="odyssey-badge" :source-badge="'odyssey'" :achieved-badge="false"></single-badge>
                <single-badge id="planner-badge" :source-badge="'planner'" :achieved-badge="true"></single-badge>
                <single-badge id="overachiever-badge" :source-badge="'overachiever'" :achieved-badge="true"></single-badge>
            </div>
        </div>
        <div id="tool-tips">
            <b-tooltip target="streaker-badge" triggers="hover">
                Streaker
            </b-tooltip>
            <b-tooltip target="wayfarer-badge" triggers="hover">
                Wayfarer
            </b-tooltip>
            <b-tooltip target="cartographer-badge" triggers="hover">
                Cartographer
            </b-tooltip>
            <b-tooltip target="writer-badge" triggers="hover">
                Writer
            </b-tooltip>
            <b-tooltip target="solver-badge" triggers="hover">
                Solver
            </b-tooltip>
            <b-tooltip target="explorer-badge" triggers="hover">
                Explorer
            </b-tooltip>
            <b-tooltip target="odyssey-badge" triggers="hover">
                Odyssey
            </b-tooltip>
            <b-tooltip target="planner-badge" triggers="hover">
                Planner
            </b-tooltip>
            <b-tooltip target="overachiever-badge" triggers="hover">
                Overachiever
            </b-tooltip>
        </div>
    </div>
</template>

<script>
    import SingleBadge from "./singleBadge";
    export default {
        name: "badgeTable",

        components: {
            SingleBadge
        }
    }
</script>

<style scoped>

</style>