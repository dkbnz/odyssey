<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <!-- The admin actions panel, which acts as the Admin Dashboard -->
        <admin-actions
                :destinations="destinations"
                :nationalityOptions="nationalityOptions"
                :profile="profile"
                :travTypeOptions="travTypeOptions"
                @admin-edit="setProfileToEdit"
                v-if="editProfile === null">
        </admin-actions>

        <!-- Once the admin has selected a profile to work on, this page becomes visible -->
        <single-profile
                :adminView="adminView"
                :destinations="destinations"
                :editProfile="editProfile"
                :key="refreshSingleProfile"
                :nationalityOptions="nationalityOptions"
                :profile="profile"
                :travTypeOptions="travTypeOptions"
                @go-back="setProfileToEdit"
                v-else>
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
                viewSingleProfile: false,
                adminView: true,
                refreshSingleProfile: 0
            }
        },
        methods: {
            /**
             * Navigates to the single profile page where an admin can achieve all functionality of the selected
             * user.
             */
            setProfileToEdit(editProfile) {
                this.editProfile = editProfile;
                this.viewSingleProfile = true;
                window.scrollTo(0, 0);
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