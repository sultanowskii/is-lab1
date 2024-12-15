from faker import Faker
from faker.providers import automotive
import yaml

BASE_DIR='sample/data'
COLORS = ['YELLOW', 'ORANGE', 'WHITE']

faker = Faker(locale='en_US')
faker.add_provider(automotive)
unique_faker: Faker = faker.unique


def generate_object():
    return dict(
        name=faker.building_number(),
        coordinates=dict(
            x=faker.random_int(0, 40),
            y=faker.random_int(0, 40),
        ),
        studentsCount=faker.random_int(10, 30),
        expelledStudents=faker.random_int(1, 5),
        transferredStudents=faker.random_int(1, 3),
        formOfEducation=faker.random_element(['DISTANCE_EDUCATION', 'FULL_TIME_EDUCATION', 'EVENING_CLASSES']),
        shouldBeExpelled=faker.random_int(1, 3),
        averageMark=faker.random_int(1, 5),
        semesterEnum=faker.random_element(['SECOND', 'FOURTH', 'EIGHTH']),
        groupAdmin=dict(
            name=faker.name(),
            eyeColor=faker.random_element(COLORS),
            hairColor=faker.random_element(COLORS),
            location=dict(
                x=faker.random_int(0, 40),
                y=faker.random_int(0, 40),
                z=faker.random_int(0, 40),
                name=faker.city(),
            
            ),
            height=faker.random_int(150, 200),
        )
    )


def generate_yaml_file(name: str, n: int):
    data = dict(
        objects=[generate_object() for _ in range(n)]
    )

    with open(f'{BASE_DIR}/{name}', 'w') as f:
        yaml.dump(data, f, default_flow_style=False)


def main():
    for i in range(9):
        generate_yaml_file(f'file{i}.yaml', faker.random_int(1, 10))
        generate_yaml_file(f'file{i}.yml', faker.random_int(1, 10))

    generate_yaml_file(f'large.yaml', 100)
    generate_yaml_file(f'large.yml', 100)


if __name__ == '__main__':
    main()
