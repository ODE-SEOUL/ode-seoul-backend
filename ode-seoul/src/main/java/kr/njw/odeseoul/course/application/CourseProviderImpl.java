package kr.njw.odeseoul.course.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.njw.odeseoul.course.application.dto.FindCourseResponse;
import kr.njw.odeseoul.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CourseProviderImpl implements CourseProvider {
    private final static int ROUTE_SRID = 4326;
    private final static String ROUTE_EPSG = "EPSG:4326";

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<FindCourseResponse> findCourses(Pair<Double, Double> searchLatLng) {
        return this.courseRepository.findAllByDeletedAtIsNullOrderById().stream().map(course -> {
            FindCourseResponse response = new FindCourseResponse();
            response.setId(course.getId());
            response.setName(course.getName());
            response.setLevel(course.getLevel());
            response.setDistance(course.getDistance());
            response.setTime(course.getTime());
            response.setDescription(course.getDescription());

            try {
                response.setCategories(this.objectMapper.readValue(course.getCategories(), new TypeReference<>() {
                }));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            response.setGugunSummary(course.getGugunSummary());
            response.setRouteSummary(course.getRouteSummary());
            response.setNearSubway(course.getNearSubway());
            response.setAccessWay(course.getAccessWay());
            response.setRegion(course.getRegion());
            response.setImage(course.getImage());

            if (course.getRoute() != null) {
                course.getRoute().setSRID(ROUTE_SRID);

                for (int i = 0; i < course.getRoute().getNumGeometries(); i++) {
                    response.getRoutes().add(
                            Arrays.stream(course.getRoute().getGeometryN(i).getCoordinates())
                                    .map(coordinate -> List.of(coordinate.getY(), coordinate.getX()))
                                    .collect(Collectors.toList())
                    );
                }

                if (searchLatLng != null) {
                    GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), ROUTE_SRID);
                    Point point = geometryFactory.createPoint(new Coordinate(searchLatLng.getSecond(), searchLatLng.getFirst())); // (lng, lat)
                    Coordinate[] coordinates = DistanceOp.nearestPoints(point, course.getRoute()); // (lng, lat)

                    try {
                        double d = JTS.orthodromicDistance(
                                new Coordinate(coordinates[0].getY(), coordinates[0].getX()), // (lat, lng)
                                new Coordinate(coordinates[1].getY(), coordinates[1].getX()), // (lat, lng)
                                CRS.decode(ROUTE_EPSG)
                        );
                        response.setDistanceFromSearchLocation(Math.round(d));
                    } catch (TransformException | FactoryException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            return response;
        }).collect(Collectors.toList());
    }
}
