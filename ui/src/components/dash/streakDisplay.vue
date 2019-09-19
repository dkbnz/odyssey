<template>
    <span v-if="currentStreak > 0">
        <span class="stylised-text">{{currentStreak}}</span>
        <b-img :src="streakerImage" fluid width="50px" >
        </b-img>
    </span>

</template>

<script>
    export default {
        name: "streakDisplay",

        props: {
            currentStreak: Number
        },

        data() {
            return {
                streakerImage: "../../../static/badges/streaker.png"
            }
        }
    }
</script>