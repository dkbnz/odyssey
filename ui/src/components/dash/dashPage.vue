<template>
    <div>
        <nav-bar-main v-bind:profile="profile"></nav-bar-main>
        <b-navbar variant="light">
            <b-navbar-nav>
                <b-nav-item @click="togglePage(viewProfile, 'view')">Profile</b-nav-item>
                <b-nav-item @click="togglePage(editProfile, 'edit')">Edit Profile</b-nav-item>
                <b-nav-item @click="togglePage(photoGallery, 'photos')">Photo Gallery</b-nav-item>
            </b-navbar-nav>
        </b-navbar>
        <view-profile v-if="viewProfile" v-bind:profile="profile" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"></view-profile>
        <edit-profile v-if="editProfile" v-bind:profile="profile" v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"></edit-profile>
        <photo-gallery v-if="photoGallery" v-bind:profile="profile"></photo-gallery>
        <footer-main></footer-main>
    </div>

</template>

<script>
    import ViewProfile from "./viewProfile.vue"
    import EditProfile from "./editProfile.vue"
    import NavBarMain from '../helperComponents/navbarMain.vue'
    import FooterMain from '../helperComponents/footerMain.vue'
    import PhotoGallery from "../photos/photoGallery";
    export default {
        name: "dashPage",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        created() {
            document.title = "TravelEA - Dashboard";
        },
        data: function() {
            return {
                viewProfile: true,
                editProfile: false,
                photoGallery: false
            }
        },
        mounted () {

        },
        methods: {
            togglePage: function(pageState, pageName) {
                if(pageName === 'view') {
                    this.viewProfile = true;
                    this.editProfile = false;
                    this.photoGallery = false;
                } else if(pageName === 'edit') {
                    this.viewProfile = false;
                    this.editProfile = true;
                    this.photoGallery = false;
                } else if(pageName === 'photos') {
                    this.viewProfile = false;
                    this.editProfile = false;
                    this.photoGallery = true;
                }
            },
        },
        components: {
            PhotoGallery,
            ViewProfile,
            EditProfile,
            NavBarMain,
            FooterMain
        }
    }
</script>

<style scoped>

</style>