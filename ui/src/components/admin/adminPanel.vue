<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <!--<single-profile></single-profile>-->
        <admin-actions :profile="profile"
                       :nationalityOptions="nationalityOptions"
                       :travTypeOptions="travTypeOptions"
                       :destinations="destinations"
                        @admin-edit="setProfileToEdit"
                       v-if="editProfile === null">
        </admin-actions>
        <single-profile
            :editProfile="editProfile"
            :profile="profile"
            v-else
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
        props: ['profile', 'nationalityOptions', 'travTypeOptions', 'destinations'],
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