<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <!--<single-profile></single-profile>-->
        <admin-actions
            v-if="editProfile === null"
            :profile="profile"
            :nationalityOptions="nationalityOptions"
            :travTypeOptions="travTypeOptions"
            @admin-edit="setProfileToEdit">
        </admin-actions>
        <single-profile
            v-else
            :editProfile="editProfile"
            :profile="profile"
            :nationalityOptions="nationalityOptions"
            :travTypeOptions="travTypeOptions"
            @go-back="setProfileToEdit">
        </single-profile>
    </div>
</template>

<script>
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import SingleProfile from './singleProfile.vue'
    import AdminActions from './adminActions.vue'

    export default {
        name: "adminPanel",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        data() {
            return {
                showSingleProfile: false,
                editProfile: null,
                viewSingleProfile: false
            }
        },
        methods: {
            /**
             * Navigates to the single profile page where an admin can achieve all functionality of the selected
             * user.
             */
            setProfileToEdit(editProfile) {
                console.log(editProfile);
                this.editProfile = editProfile;
                this.viewSingleProfile = true;
                //this.$router.push('/singleProfile')
            }
        },
        components: {
            NavBarMain,
            SingleProfile,
            AdminActions
        }
    }
</script>

<style scoped>
</style>