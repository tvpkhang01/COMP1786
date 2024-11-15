import React, { useEffect, useState } from "react";
import { View, Text, Button, StyleSheet } from "react-native";
import { useRoute, useNavigation } from "@react-navigation/native";
import { ref, onValue } from "firebase/database";
import { database } from "../firebaseConfig";

export default function ClassDetailScreen() {
  const route = useRoute();
  const navigation = useNavigation();
  const { classId } = route.params;
  const [classDetails, setClassDetails] = useState(null);

  useEffect(() => {
    const classRef = ref(database, `classes/${classId}`);
    const unsubscribe = onValue(classRef, (snapshot) => {
      const classData = snapshot.val();
      setClassDetails(classData);
    });

    return () => unsubscribe();
  }, [classId]);

  if (!classDetails) return <Text style={styles.loadingText}>Loading...</Text>;

  return (
    <View style={styles.container}>
      <Button title="Back" onPress={() => navigation.goBack()} />
      <Text style={styles.title}>Class Details</Text>
      <Text style={styles.label}>Teacher:</Text>
      <Text style={styles.value}>{classDetails.teacher}</Text>
      <Text style={styles.label}>Date:</Text>
      <Text style={styles.value}>{classDetails.date}</Text>
      <Text style={styles.label}>Comments:</Text>
      <Text style={styles.value}>{classDetails.comments}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    backgroundColor: "#f8f9fa",
  },
  loadingText: {
    fontSize: 18,
    textAlign: "center",
    marginTop: 20,
  },
  title: {
    fontSize: 24,
    fontWeight: "bold",
    marginBottom: 16,
  },
  label: {
    fontSize: 18,
    fontWeight: "600",
    marginTop: 12,
  },
  value: {
    fontSize: 16,
    marginBottom: 8,
  },
});
