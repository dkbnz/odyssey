<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <!-- The admin actions panel, which acts as the Admin Dashboard -->
        <admin-actions
                v-if="editProfile === null"
                :profile="profile"
                :nationalityOptions="nationalityOptions"
                :travTypeOptions="travTypeOptions"
                :destinations="destinations"
                @admin-edit="setProfileToEdit">
        </admin-actions>

        <!-- Once the admin has selected a profile to work on, this page becomes visible -->
        <single-profile
                v-else
                :key="refreshSingleProfile"
                :adminView="adminView"
                :editProfile="editProfile"
                :profile="profile"
                :nationalityOptions="nationalityOptions"
                :travTypeOptions="travTypeOptions"
                :destinations="destinations"
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