import React, { useEffect, useState } from "react";
import { View, Text, Button, FlatList, StyleSheet } from "react-native";
import { useNavigation, useRoute } from "@react-navigation/native";
import { database } from "../firebaseConfig";
import { onValue, ref } from "firebase/database";

export default function ClassesScreen() {
  const navigation = useNavigation();
  const route = useRoute();
  const { courseId } = route.params;
  const [classes, setClasses] = useState([]);

  console.log(classes);

  useEffect(() => {
    const classesRef = ref(database, `classes/`);
    const showData = onValue(classesRef, (snapshot) => {
      const classesData = snapshot.val();
      const classesList = classesData
        ? Object.keys(classesData)
            .map((key) => ({
              id: key,
              ...classesData[key],
            }))
            .filter((classItem) => classItem.courseId === courseId)
        : [];
      setClasses(classesList);
    });

    return () => showData();
  }, [courseId]);

  if (classes.length === 0) {
    return (
      <View style={styles.container}>
        <Text style={styles.loadingText}>No classes available</Text>
        <Button title="Back" onPress={() => navigation.goBack()} />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Button title="Back" onPress={() => navigation.goBack()} />
      <FlatList
        data={classes}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <View style={styles.classItem}>
            <Text style={styles.className}>Class {item.id}</Text>
            <Button
              title="More"
              onPress={() =>
                navigation.navigate("ClassDetail", { classId: item.id })
              }
              color="#007bff"
            />
          </View>
        )}
      />
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
  loadingText: {
    fontSize: 18,
    textAlign: "center",
    marginTop: 20,
    marginBottom: 20,
  },
  classItem: {
    padding: 16,
    marginVertical: 8,
    backgroundColor: "#ffffff",
    borderRadius: 8,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 3,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  className: {
    fontSize: 18,
    fontWeight: "600",
  },
});
