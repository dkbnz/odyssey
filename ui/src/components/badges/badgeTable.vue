<template>
    <div style="max-width: 500px">
        <b-list-group>
            <b-list-group-item href="#"
                               class="flex-column align-items-start"
                               v-for="badge in profile.achievementTracker.badges"
                               :key="badge.id">
                <b-row>
                    <b-col cols="4">
                        <single-badge :id="badge.name + 'badge'" :source-badge="badge.name.toLowerCase()" :achieved-badge="true" :achievement-level="badge.level"></single-badge>
                        <b-tooltip :target="badge.name + 'badge'" triggers="hover">
                            {{badge.name}}
                        </b-tooltip>
                    </b-col>
                    <b-col>
                        <div class="d-flex w-100 justify-content-between">
                            <h5 class="mb-1">{{badge.name}}</h5>
                            <small>{{badge.progress}}{{badge.breakpoint == null ? '' : '/' + badge.breakpoint}}</small>
                        </div>

                        <p class="mb-1">
                            {{badge.howToProgress}}
                        </p>

                        <b-progress :value="badge.progress" :max="badge.breakpoint == null ? badge.progress : badge.breakpoint" :variant="badge.breakpoint == null ? 'success' : 'primary'"></b-progress>
                    </b-col>
                </b-row>
            </b-list-group-item>
        </b-list-group>

        <!--<div class="row">-->


            <!--<div class="column">-->
                <!--<single-badge id="streaker-badge" :source-badge="profile.achievementTracker.badges[0].name.toLowerCase()" :achieved-badge="true" :achievement-level="profile.achievementTracker.badges[0].level"></single-badge>-->
                <!--<single-badge id="wayfarer-badge" :source-badge="'wayfarer'" :achieved-badge="true"></single-badge>-->
                <!--<single-badge id="cartographer-badge" :source-badge="'cartographer'" :achieved-badge="true"></single-badge>-->
            <!--</div>-->
            <!--<div class="column">-->
                <!--<single-badge id="writer-badge" :source-badge="'writer'" :achieved-badge="true"></single-badge>-->
                <!--<single-badge id="solver-badge" :source-badge="'solver'" :achieved-badge="true"></single-badge>-->
                <!--<single-badge id="explorer-badge" :source-badge="'explorer'" :achieved-badge="false"></single-badge>-->
            <!--</div>-->
            <!--<div class="column">-->
                <!--<single-badge id="odyssey-badge" :source-badge="'odyssey'" :achieved-badge="false"></single-badge>-->
                <!--<single-badge id="planner-badge" :source-badge="'planner'" :achieved-badge="true"></single-badge>-->
                <!--<single-badge id="overachiever-badge" :source-badge="'overachiever'" :achieved-badge="true"></single-badge>-->
            <!--</div>-->
        <!--</div>-->
        <!--<div id="tool-tips">-->
            <!--<b-tooltip target="streaker-badge" triggers="hover">-->
                <!--Streaker-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="wayfarer-badge" triggers="hover">-->
                <!--Wayfarer-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="cartographer-badge" triggers="hover">-->
                <!--Cartographer-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="writer-badge" triggers="hover">-->
                <!--Writer-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="solver-badge" triggers="hover">-->
                <!--Solver-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="explorer-badge" triggers="hover">-->
                <!--Explorer-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="odyssey-badge" triggers="hover">-->
                <!--Odyssey-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="planner-badge" triggers="hover">-->
                <!--Planner-->
            <!--</b-tooltip>-->
            <!--<b-tooltip target="overachiever-badge" triggers="hover">-->
                <!--Overachiever-->
            <!--</b-tooltip>-->
        <!--</div>-->
    </div>
</template>

<script>
    import SingleBadge from "./singleBadge";
    export default {
        name: "badgeTable",

        props: {
            profile: Object
        },

        components: {
            SingleBadge
        }
    }
</script>

<style scoped>

</style>