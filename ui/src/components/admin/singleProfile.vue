<template>
    <div>
        <b-row>
            <b-col cols="2">
                <b-navbar toggleable="lg">
                    <b-collapse id="nav-collapse-admin" is-nav>
                        <b-nav vertical class="singleProfileNav">
                            <b-navbar-brand>
                                <b-img v-bind="profileImage" blank-color="#777" rounded="circle" alt="Circle image">
                                </b-img>
                            </b-navbar-brand>
                            <b-nav-item>Add Trips</b-nav-item>
                            <b-nav-item>View Trips</b-nav-item>
                            <b-nav-item>Edit Profile</b-nav-item>
                        </b-nav>
                    </b-collapse>
                    <b-navbar-toggle target="nav-collapse-admin"></b-navbar-toggle>
                </b-navbar>

            </b-col>

            <b-col cols="8" class="singleProfile">
                Rest of the page
            </b-col>
        </b-row>
    </div>
</template>

<script>
    import NavBarMain from '../helperComponents/navbarMain.vue'

    export default {
        name: "singleProfile",
        props: ['adminView', 'profile', 'editProfile'],
        components: {
            NavBarMain
        },
        data() {
            return {
                profileImage: {blank: true, width: 75, height: 75, class: 'm1'}
            }
        }
    }
</script>

<style scoped>
    .singleProfile {
        margin: 10px;
        background: white;
        border-radius: 5px;
    }

    .singleProfileNav {
        background: white;
    }

    a {
        color: grey;
    }
</style>