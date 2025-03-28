// Logout page. Test this out since I'm not sure when this will be seen and if it'll need to be edited.

<template>
  <h1>Logout</h1>
</template>

<script>
export default {
  created() {
    this.$store.commit("LOGOUT");
    this.$router.push("/");
  }
};
</script>
