<template>
    <div>
        <h3>Public Photos</h3>
        <photo-table :photos="publicPhotos" :showDropdown="false">
        </photo-table>

        <h3>Your Photos</h3>
        <photo-table :photos="personalPhotos"
                     :profile="profile"
                     :userProfile="userProfile"
                     @privacy-update="updatePhotoPrivacy">
        </photo-table>
        <h3>Personal Photo Gallery</h3>
        <photo-table :selectedImages="personalPhotos"
                     :photos="profile.photoGallery"
                     :profile="profile"
                     :userProfile="userProfile"
                     @privacy-update="updatePhotoPrivacy"
                     @photo-click="photoToggled">
        </photo-table>
    </div>
</template>

<script>
    import PhotoTable from "../photos/photoTable";

    export default {
        name: "destinationGallery",
        data: function () {
            return {
                publicPhotos: [],
                personalPhotos: []
            }
        },

        props: {
            destination: Object,
            profile: Object,
            userProfile: Object
        },

        mounted() {
            this.calculatePhotoSplit();
        },

        methods: {

            calculatePhotoSplit() {
                this.publicPhotos = [];
                this.personalPhotos = [];
                for (let i = 0; i < this.destination.photoGallery.length; i++) {
                    if (this.destination.photoGallery[i].public === "true"
                    || this.destination.photoGallery[i].public) {
                        this.publicPhotos.push(this.destination.photoGallery[i]);
                    }
                    if (this.containsById(this.profile.photoGallery, this.destination.photoGallery[i])) {
                        this.personalPhotos.push(this.destination.photoGallery[i])
                    }
                }
            },

            /**
             * Updates the photos list sent to the photoTable for a single privacy photo.
             *
             * @param photo           the photo that's changing status.
             */
            updatePhotoPrivacy: function(photo) {
                let self = this;

                fetch('/v1/photos', {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(photo)
                }).then(response => {
                    if (response.status === 200) {
                        response.clone().json().then(text => {
                            self.profile.photoGallery = text;
                        });
                    }
                });

                this.calculatePhotoSplit();
            },

            containsById(arrayToCheck, object) {
                for (let i = 0; i < arrayToCheck.length; i++) {
                    if(arrayToCheck[i].id === object.id) {
                        return true;
                    }
                }
                return false;
            },

            photoToggled(photo) {
                if (this.containsById(this.destination.photoGallery, photo)) {
                    console.log("Contains")
                    this.destination.photoGallery.pop(this.destination.photoGallery.indexOf(photo))
                } else {
                    console.log("Not contains")
                    this.destination.photoGallery.push(photo)
                }
                this.calculatePhotoSplit()
                console.log(this.publicPhotos)
            }

        },

        components: {
            PhotoTable
        }
    }
</script>

<style scoped>

</style>