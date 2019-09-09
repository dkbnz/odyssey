<template>
    <div>
        <b-img :src="assets[sourceBadge]" :class="{disabledBadge: !achievedBadge}"></b-img>
        <div>
            <b-img v-for="index in achievementLevel"
                   :id="'star-' + index"
                   :src="assets['star']"
                   :class="{disabledBadge: !achievedBadge}">
            </b-img>
        </div>

    </div>
</template>

<script>
    import assets from '../../assets/assets';
    export default {
        name: "singleBadge",

        props: {
            sourceBadge: String,
            achievedBadge: Boolean,
            achievementLevel: Number
        },

        computed: {
            assets() {
                return assets
            }
        }
    }
</script>