<template>
    <div>
        <b-list-group>
            <b-list-group-item href="#"
                               class="flex-column align-items-start"
                               v-for="badge in profile.achievementTracker.badges"
                               :key="badge.id">
                <b-row>
                    <b-col cols="4">
                        <single-badge :badge="badge"></single-badge>
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