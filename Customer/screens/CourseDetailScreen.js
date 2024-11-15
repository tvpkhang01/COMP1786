import React, { useEffect, useState } from "react";
import { View, Text, Button, StyleSheet } from "react-native";
import { useRoute, useNavigation } from "@react-navigation/native";
import { ref, onValue } from "firebase/database";
import { database } from "../firebaseConfig";

export default function CourseDetailScreen() {
  const route = useRoute();
  const navigation = useNavigation();
  const { courseId } = route.params;
  const [course, setCourse] = useState(null);

  useEffect(() => {
    const courseRef = ref(database, `courses/${courseId}`);
    const unsubscribe = onValue(courseRef, (snapshot) => {
      const courseData = snapshot.val();
      setCourse(courseData);
    });

    return () => unsubscribe();
  }, [courseId]);

  if (!course) return <Text>Loading...</Text>;

  return (
    <View style={styles.container}>
      <Button title="Back" onPress={() => navigation.goBack()} />
      <Text style={styles.label}>Days:</Text>
      <Text style={styles.value}>{course.dayOfWeek}</Text>
      <Text style={styles.label}>Time:</Text>
      <Text style={styles.value}>{course.time}</Text>
      <Text style={styles.label}>Capacity:</Text>
      <Text style={styles.value}>{course.capacity} people</Text>
      <Text style={styles.label}>Duration:</Text>
      <Text style={styles.value}>{course.duration} minutes</Text>
      <Text style={styles.label}>Price:</Text>
      <Text style={styles.value}>€{course.price}</Text>
      <Text style={styles.label}>Description:</Text>
      <Text style={styles.value}>{course.description}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    paddingTop: 55,
    backgroundColor: "#f8f9fa",
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
