<template>
    <div>

        <b-img :id="badge.name + 'badge'" :src="assets[badge.name.toLowerCase()]" :class="{disabledBadge: !badge.level}" fluid></b-img>
        <div class="rowStars">
            <div class="columnStars" v-for="index in 3" :key="index">
                <b-img
                        :id="'star-' + index"
                        :src="assets[starSources[index-1]]"
                        :class="{disabledBadge: index > badge.level}" fluid>
                </b-img>
            </div>
        </div>

        <b-tooltip :target="badge.name + 'badge'" triggers="hover">
            {{badge.name}}
        </b-tooltip>

    </div>
</template>

<script>
    export default {
        name: "singleBadge",

        data() {
            return {
                starSources: ['bronzeStar', 'silverStar', 'goldStar']
            }
        },

        props: {
            badge: Object
        }
    }
</script>